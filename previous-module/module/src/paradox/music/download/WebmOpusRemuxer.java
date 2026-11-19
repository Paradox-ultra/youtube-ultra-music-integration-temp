package paradox.music.download;

/*
 * Copyright (C) Clash-Projects / LastWave contributors
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 *
 * Adapted from LastWave native WebmOpusRemuxer.kt.
 * Losslessly moves Opus packets from YouTube's WebM container into Ogg Opus (.opus).
 * No audio is decoded or re-encoded, so the downloaded stream stays bit-exact.
 *
 * Features dual-engine extraction:
 * 1. Pure Java EBML/Matroska parser (runs everywhere including desktop JVM regression tests
 *    without Android framework stub dependencies).
 * 2. Android framework MediaExtractor fallback (for device compatibility with exotic mux variations).
 */

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class WebmOpusRemuxer {
    private static final String TAG = "WebmOpusRemuxer";
    private static final String OGG_CAPTURE_PATTERN = "OggS";
    private static final String OPUS_HEAD = "OpusHead";
    private static final String OPUS_TAGS = "OpusTags";
    private static final int OGG_MAX_SEGMENTS = 255;
    private static final int TARGET_PAGE_PAYLOAD = 60 * 1024;
    private static final int MAX_PACKET_BYTES = 2 * 1024 * 1024;
    private static final int OGG_CRC_POLYNOMIAL = 0x04C11DB7;

    private static final long EBML_HEADER = 0x1A45DFA3L;
    private static final long SEGMENT = 0x18538067L;
    private static final long INFO = 0x1549A966L;
    private static final long TRACKS = 0x1654AE6BL;
    private static final long CLUSTER = 0x1F43B675L;
    private static final long CUES = 0x1C53BB6BL;
    private static final long TAGS = 0x1254C367L;
    private static final long SEEKHEAD = 0x114D9B74L;
    private static final long CHAPTERS = 0x1043A770L;
    private static final long ATTACH = 0x1941A469L;

    private static final int[] oggCrcTable = new int[256];

    static {
        for (int i = 0; i < 256; i++) {
            int remainder = i << 24;
            for (int bit = 0; bit < 8; bit++) {
                if ((remainder & 0x80000000) != 0) {
                    remainder = (remainder << 1) ^ OGG_CRC_POLYNOMIAL;
                } else {
                    remainder = remainder << 1;
                }
            }
            oggCrcTable[i] = remainder;
        }
    }

    public static boolean remux(File inputWebm, File outputOpus) {
        if (inputWebm == null || !inputWebm.isFile() || inputWebm.length() <= 0 || inputWebm.equals(outputOpus)) {
            return false;
        }
        if (outputOpus != null && outputOpus.exists() && outputOpus.length() > 0) {
            return false;
        }

        // 1. Try pure Java EBML extraction first (no Android framework stub dependency)
        try {
            if (remuxPureJava(inputWebm, outputOpus)) {
                return true;
            }
        } catch (Throwable t) {
            logW("Pure Java remux failed, attempting MediaExtractor fallback: " + t.getMessage());
        }

        // 2. Fallback to MediaExtractor on Android devices if pure Java encounters an unsupported structure
        return remuxViaMediaExtractor(inputWebm, outputOpus);
    }

    /**
     * Pure Java WebM Opus extractor and Ogg packager.
     * Operates completely in pure Java without calling Android MediaExtractor.
     */
    public static boolean remuxPureJava(File inputWebm, File outputOpus) {
        if (inputWebm == null || !inputWebm.isFile() || inputWebm.length() <= 0 || inputWebm.equals(outputOpus)) {
            return false;
        }
        if (outputOpus != null && outputOpus.exists() && outputOpus.length() > 0) {
            return false;
        }

        boolean completed = false;
        try (Rd r = new Rd(inputWebm)) {
            long magic = readId(r);
            if (magic != EBML_HEADER) return false;
            long hs = readSize(r);
            if (hs < 0) return false;
            r.pos += hs;

            long opusTrackNum = -1;
            byte[] opusHead = null;
            long timecodeScale = 1_000_000L;
            double durationMs = 0;

            // Locate segment
            long segPos = -1;
            long segLen = -1;
            while (r.pos < r.len) {
                long sid = readId(r);
                long ss = readSize(r);
                if (sid != SEGMENT) {
                    if (ss < 0) break;
                    r.pos += ss;
                    continue;
                }
                segPos = r.pos;
                segLen = ss < 0 ? r.len - segPos : ss;
                break;
            }
            if (segPos < 0) return false;

            long segEnd = segPos + segLen;

            // Header scan: Find tracks and audio info
            while (r.pos < segEnd) {
                long eid;
                long es;
                try {
                    eid = readId(r);
                    es = readSize(r);
                } catch (EOFException e) {
                    break;
                }
                long ebody = r.pos;
                long eend = es < 0 ? segEnd : ebody + es;

                if (eid == INFO) {
                    while (r.pos < eend) {
                        long c = readId(r), cs = readSize(r);
                        if (c == 0x2AD7B1L) timecodeScale = readUint(r, cs);
                        else if (c == 0x4489L) durationMs = readFloat(r, cs);
                        else r.pos += cs;
                    }
                } else if (eid == TRACKS) {
                    while (r.pos < eend) {
                        long c = readId(r), cs = readSize(r);
                        long tb = r.pos;
                        if (c == 0xAEL) { // TrackEntry
                            long tNum = -1, tType = -1;
                            String codecId = "";
                            byte[] priv = null;
                            while (r.pos < tb + cs) {
                                long tc = readId(r), tcs = readSize(r);
                                long tcb = r.pos;
                                if (tc == 0xD7L) tNum = readUint(r, tcs);
                                else if (tc == 0x83L) tType = readUint(r, tcs);
                                else if (tc == 0x86L) codecId = new String(r.bytes((int)tcs), StandardCharsets.ISO_8859_1).trim();
                                else if (tc == 0x63A2L) priv = r.bytes((int)tcs);
                                r.pos = tcb + tcs;
                            }
                            if (codecId.equalsIgnoreCase("A_OPUS") || (tType == 2 && priv != null && priv.length >= 19 && startsWithAscii(priv, OPUS_HEAD))) {
                                opusTrackNum = tNum;
                                opusHead = priv;
                            }
                        }
                        r.pos = tb + cs;
                    }
                    break; // Header scan done, ready to stream clusters
                } else {
                    if (es < 0) break;
                    r.pos = eend;
                }
            }

            if (opusTrackNum < 0 || opusHead == null || !startsWithAscii(opusHead, OPUS_HEAD) || opusHead.length < 19) {
                return false;
            }

            int preSkip = (opusHead[10] & 0xFF) | ((opusHead[11] & 0xFF) << 8);
            long declaredDurationUs = (long)(durationMs * (timecodeScale / 1000.0));

            File parent = outputOpus.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) return false;

            int serialNumber = (int)(inputWebm.length() ^ System.nanoTime());
            if (serialNumber == 0) serialNumber = 1;

            int sequenceNumber = 0;
            long totalSamples = 0;
            long previousPageGranule = 0;
            int lastPacketSamples = 0;
            int packetCount = 0;

            ByteArrayOutputStream pageLacing = new ByteArrayOutputStream();
            ByteArrayOutputStream pagePayload = new ByteArrayOutputStream();

            try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputOpus))) {
                writePacketPage(output, opusHead, 0x02, 0L, serialNumber, sequenceNumber++);
                writePacketPage(output, emptyOpusTags(), 0, 0L, serialNumber, sequenceNumber++);

                while (r.pos < segEnd) {
                    long eid;
                    long es;
                    try {
                        eid = readId(r);
                        es = readSize(r);
                    } catch (EOFException e) {
                        break;
                    }
                    long ebody = r.pos;
                    long eend = es < 0 ? segEnd : ebody + es;

                    if (eid == CLUSTER) {
                        while (r.pos < eend) {
                            long c;
                            try { c = readId(r); } catch (EOFException e) { break; }
                            if (es < 0 && isTopLevel(c)) { r.pos -= 4; break; }
                            long cs = readSize(r);
                            long cb = r.pos;
                            if (c == 0xA3L) { // SimpleBlock
                                long tn = readSize(r);
                                int relTc = (short)r.u16();
                                int flags = r.u8();
                                int dlen = (int)(cb + cs - r.pos);
                                if (tn == opusTrackNum && dlen > 0) {
                                    byte[] packet = r.bytes(dlen);
                                    int sampleCount = opusPacketDurationSamples(packet);
                                    if (sampleCount <= 0) sampleCount = 960;

                                    byte[] lacing = packetLacing(packet.length);
                                    if (pageLacing.size() + lacing.length > OGG_MAX_SEGMENTS || pagePayload.size() + packet.length > TARGET_PAGE_PAYLOAD) {
                                        flushAudioPage(output, pageLacing, pagePayload, totalSamples, lastPacketSamples, previousPageGranule, declaredDurationUs, preSkip, serialNumber, sequenceNumber++, false);
                                        previousPageGranule = totalSamples;
                                    }

                                    pageLacing.write(lacing);
                                    pagePayload.write(packet);
                                    totalSamples += sampleCount;
                                    lastPacketSamples = sampleCount;
                                    packetCount++;
                                }
                            }
                            r.pos = cb + cs;
                        }
                    } else {
                        if (es < 0) break;
                        r.pos = eend;
                    }
                }

                flushAudioPage(output, pageLacing, pagePayload, totalSamples, lastPacketSamples, previousPageGranule, declaredDurationUs, preSkip, serialNumber, sequenceNumber++, true);
                output.flush();
            }

            completed = isValidOgg(outputOpus) && packetCount > 0;
            return completed;
        } catch (Exception error) {
            logW("Pure Java remux error on " + inputWebm.getName() + ": " + error.getMessage());
            return false;
        } finally {
            if (!completed && outputOpus != null) {
                try { outputOpus.delete(); } catch (Exception ignore) {}
            }
        }
    }

    private static boolean remuxViaMediaExtractor(File inputWebm, File outputOpus) {
        MediaExtractor extractor = null;
        boolean completed = false;
        try {
            extractor = new MediaExtractor();
            extractor.setDataSource(inputWebm.getAbsolutePath());
            int trackIndex = -1;
            int trackCount = extractor.getTrackCount();
            for (int i = 0; i < trackCount; i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);
                if (mime != null && mime.equalsIgnoreCase("audio/opus")) {
                    trackIndex = i;
                    break;
                }
            }
            if (trackIndex < 0) return false;

            MediaFormat format = extractor.getTrackFormat(trackIndex);
            byte[] opusHead = getFormatByteArray(format, "csd-0");
            if (opusHead == null || !startsWithAscii(opusHead, OPUS_HEAD) || opusHead.length < 19) {
                return false;
            }

            int preSkip = (opusHead[10] & 0xFF) | ((opusHead[11] & 0xFF) << 8);
            long declaredDurationUs = 0;
            if (format.containsKey(MediaFormat.KEY_DURATION)) {
                try { declaredDurationUs = Math.max(0, format.getLong(MediaFormat.KEY_DURATION)); } catch (Exception ignore) {}
            }

            extractor.selectTrack(trackIndex);
            File parent = outputOpus.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) return false;

            int serialNumber = (int)(inputWebm.length() ^ System.nanoTime());
            if (serialNumber == 0) serialNumber = 1;

            int sequenceNumber = 0;
            long totalSamples = 0;
            long previousPageGranule = 0;
            int lastPacketSamples = 0;
            int packetCount = 0;

            int maxInputSize = 64 * 1024;
            if (format.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
                try {
                    int sz = format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
                    maxInputSize = Math.max(4 * 1024, Math.min(sz, MAX_PACKET_BYTES));
                } catch (Exception ignore) {}
            }
            ByteBuffer sampleBuffer = ByteBuffer.allocateDirect(maxInputSize);

            try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputOpus))) {
                writePacketPage(output, opusHead, 0x02, 0L, serialNumber, sequenceNumber++);
                writePacketPage(output, emptyOpusTags(), 0, 0L, serialNumber, sequenceNumber++);

                ByteArrayOutputStream pageLacing = new ByteArrayOutputStream();
                ByteArrayOutputStream pagePayload = new ByteArrayOutputStream();

                while (true) {
                    sampleBuffer.clear();
                    int sampleSize = extractor.readSampleData(sampleBuffer, 0);
                    if (sampleSize < 0) {
                        flushAudioPage(output, pageLacing, pagePayload, totalSamples, lastPacketSamples, previousPageGranule, declaredDurationUs, preSkip, serialNumber, sequenceNumber++, true);
                        break;
                    }

                    byte[] packet = new byte[sampleSize];
                    sampleBuffer.get(packet);
                    int sampleCount = opusPacketDurationSamples(packet);
                    if (sampleCount <= 0) sampleCount = 960; // fallback standard 20ms frame

                    byte[] lacing = packetLacing(packet.length);
                    if (pageLacing.size() + lacing.length > OGG_MAX_SEGMENTS || pagePayload.size() + packet.length > TARGET_PAGE_PAYLOAD) {
                        flushAudioPage(output, pageLacing, pagePayload, totalSamples, lastPacketSamples, previousPageGranule, declaredDurationUs, preSkip, serialNumber, sequenceNumber++, false);
                        previousPageGranule = totalSamples;
                    }

                    pageLacing.write(lacing);
                    pagePayload.write(packet);
                    totalSamples += sampleCount;
                    lastPacketSamples = sampleCount;
                    packetCount++;

                    extractor.advance();
                }
                output.flush();
            }

            completed = isValidOgg(outputOpus) && packetCount > 0;
            return completed;
        } catch (Exception error) {
            logW("Could not remux " + (inputWebm != null ? inputWebm.getName() : "null") + " to Ogg Opus via MediaExtractor: " + error.getMessage());
            return false;
        } finally {
            if (extractor != null) {
                try { extractor.release(); } catch (Exception ignore) {}
            }
            if (!completed && outputOpus != null) {
                try { outputOpus.delete(); } catch (Exception ignore) {}
            }
        }
    }

    private static void logW(String msg) {
        try {
            Log.w(TAG, msg);
        } catch (Throwable ignore) {
            System.err.println("[" + TAG + "] " + msg);
        }
    }

    private static void flushAudioPage(OutputStream output, ByteArrayOutputStream pageLacing, ByteArrayOutputStream pagePayload,
                                       long totalSamples, int lastPacketSamples, long previousPageGranule,
                                       long declaredDurationUs, int preSkip, int serialNumber, int sequenceNumber, boolean endOfStream) throws IOException {
        if (pageLacing.size() == 0) return;
        long pageGranule = totalSamples;
        if (endOfStream && declaredDurationUs > 0) {
            long declaredEnd = (long)preSkip + ((declaredDurationUs * 48000L + 500000L) / 1000000L);
            long earliestValidEnd = Math.max(previousPageGranule, totalSamples - (long)lastPacketSamples);
            if (declaredEnd >= earliestValidEnd && declaredEnd <= totalSamples) {
                pageGranule = declaredEnd;
            }
        }
        writeOggPage(output, endOfStream ? 0x04 : 0, pageGranule, serialNumber, sequenceNumber, pageLacing.toByteArray(), pagePayload.toByteArray());
        pageLacing.reset();
        pagePayload.reset();
    }

    private static byte[] emptyOpusTags() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(OPUS_TAGS.getBytes(StandardCharsets.US_ASCII));
            byte[] vendor = "UltraMusic".getBytes(StandardCharsets.UTF_8);
            writeLe32(out, vendor.length);
            out.write(vendor);
            writeLe32(out, 0); // user comment list length = 0
        } catch (IOException ignore) {}
        return out.toByteArray();
    }

    private static byte[] packetLacing(int packetSize) {
        int fullSegments = packetSize / 255;
        byte[] lacing = new byte[fullSegments + 1];
        for (int i = 0; i < fullSegments; i++) {
            lacing[i] = (byte) 255;
        }
        lacing[fullSegments] = (byte) (packetSize % 255);
        return lacing;
    }

    private static void writePacketPage(OutputStream output, byte[] packet, int headerType, long granulePosition,
                                        int serialNumber, int sequenceNumber) throws IOException {
        writeOggPage(output, headerType, granulePosition, serialNumber, sequenceNumber, packetLacing(packet.length), packet);
    }

    private static void writeOggPage(OutputStream output, int headerType, long granulePosition,
                                     int serialNumber, int sequenceNumber, byte[] lacing, byte[] data) throws IOException {
        if (lacing.length > OGG_MAX_SEGMENTS) throw new IllegalArgumentException("Lacing segments exceed maximum");
        int expectedLen = 0;
        for (byte b : lacing) expectedLen += (b & 0xFF);
        if (expectedLen != data.length) throw new IllegalArgumentException("Lacing size mismatch");

        ByteArrayOutputStream page = new ByteArrayOutputStream(27 + lacing.length + data.length);
        page.write(OGG_CAPTURE_PATTERN.getBytes(StandardCharsets.US_ASCII));
        page.write(0); // version
        page.write(headerType & 0xFF);
        writeLe64(page, granulePosition);
        writeLe32(page, serialNumber);
        writeLe32(page, sequenceNumber);
        writeLe32(page, 0); // crc placeholder
        page.write(lacing.length);
        page.write(lacing);
        page.write(data);

        byte[] bytes = page.toByteArray();
        int crc = oggCrc(bytes);
        bytes[22] = (byte) crc;
        bytes[23] = (byte) (crc >>> 8);
        bytes[24] = (byte) (crc >>> 16);
        bytes[25] = (byte) (crc >>> 24);
        output.write(bytes);
    }

    private static int opusPacketDurationSamples(byte[] packet) {
        if (packet == null || packet.length == 0) return 0;
        int toc = packet[0] & 0xFF;
        int configuration = toc >>> 3;
        int samplesPerFrame;
        if (configuration < 12) {
            switch (configuration & 0x03) {
                case 0: samplesPerFrame = 480; break;
                case 1: samplesPerFrame = 960; break;
                case 2: samplesPerFrame = 1920; break;
                default: samplesPerFrame = 2880; break;
            }
        } else if (configuration < 16) {
            samplesPerFrame = ((configuration & 0x01) == 0) ? 480 : 960;
        } else {
            samplesPerFrame = 120 << (configuration & 0x03);
        }

        int frameCount;
        switch (toc & 0x03) {
            case 0: frameCount = 1; break;
            case 1:
            case 2: frameCount = 2; break;
            default:
                if (packet.length >= 2) frameCount = packet[1] & 0x3F;
                else return 0;
                break;
        }
        if (frameCount < 1 || frameCount > 48) return 0;
        int total = samplesPerFrame * frameCount;
        return (total >= 120 && total <= 5760) ? total : 0;
    }

    private static byte[] getFormatByteArray(MediaFormat format, String key) {
        try {
            ByteBuffer buf = format.getByteBuffer(key);
            if (buf == null) return null;
            ByteBuffer dup = buf.duplicate();
            dup.rewind();
            byte[] b = new byte[dup.remaining()];
            dup.get(b);
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean startsWithAscii(byte[] data, String prefix) {
        byte[] p = prefix.getBytes(StandardCharsets.US_ASCII);
        if (data.length < p.length) return false;
        for (int i = 0; i < p.length; i++) {
            if (data[i] != p[i]) return false;
        }
        return true;
    }

    private static void writeLe32(OutputStream out, int value) throws IOException {
        for (int i = 0; i < 4; i++) {
            out.write((value >>> (i * 8)) & 0xFF);
        }
    }

    private static void writeLe64(OutputStream out, long value) throws IOException {
        for (int i = 0; i < 8; i++) {
            out.write((int)((value >>> (i * 8)) & 0xFF));
        }
    }

    private static int oggCrc(byte[] bytes) {
        int crc = 0;
        for (byte b : bytes) {
            int index = ((crc >>> 24) ^ (b & 0xFF)) & 0xFF;
            crc = (crc << 8) ^ oggCrcTable[index];
        }
        return crc;
    }

    private static boolean isValidOgg(File file) {
        if (file == null || file.length() < 64) return false;
        try (FileInputStream in = new FileInputStream(file)) {
            byte[] header = new byte[4];
            return in.read(header) == 4 && new String(header, StandardCharsets.US_ASCII).equals(OGG_CAPTURE_PATTERN);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isTopLevel(long id) {
        return id == CLUSTER || id == CUES || id == TAGS || id == INFO || id == TRACKS || id == SEEKHEAD || id == CHAPTERS || id == ATTACH;
    }

    private static long readId(Rd r) throws IOException {
        int b = r.u8();
        int len = b >= 0x80 ? 1 : b >= 0x40 ? 2 : b >= 0x20 ? 3 : b >= 0x10 ? 4 : 0;
        if (len == 0) throw new IOException("bad EBML id");
        long v = b;
        for (int i = 1; i < len; i++) v = (v << 8) | r.u8();
        return v;
    }

    private static long readSize(Rd r) throws IOException {
        int b = r.u8();
        int len = 1;
        int mask = 0x80;
        while (len <= 8 && (b & mask) == 0) {
            len++;
            mask >>= 1;
        }
        if (len > 8) throw new IOException("bad EBML size");
        long v = b & (mask - 1);
        boolean ones = (v == mask - 1);
        for (int i = 1; i < len; i++) {
            int c = r.u8();
            v = (v << 8) | c;
            if (c != 0xff) ones = false;
        }
        return ones ? -1 : v;
    }

    private static long readUint(Rd r, long size) throws IOException {
        return r.u((int)size);
    }

    private static double readFloat(Rd r, long size) throws IOException {
        if (size == 4) return Float.intBitsToFloat((int)r.u32());
        if (size == 8) return Double.longBitsToDouble(r.u64());
        r.pos += size;
        return 0.0;
    }

    /** Buffered random-access file reader. Pure Java. */
    static final class Rd implements Closeable {
        final RandomAccessFile f;
        final long len;
        final byte[] buf = new byte[1 << 18];
        long bufPos = -1;
        int bufLen;
        long pos;

        Rd(File file) throws IOException {
            f = new RandomAccessFile(file, "r");
            len = f.length();
        }

        int u8() throws IOException {
            if (bufPos < 0 || pos < bufPos || pos >= bufPos + bufLen) {
                if (pos >= len) throw new EOFException("unexpected end of file at " + pos);
                f.seek(pos);
                bufPos = pos;
                bufLen = f.read(buf, 0, buf.length);
                if (bufLen <= 0) {
                    bufPos = -1;
                    throw new EOFException("read failed at " + pos);
                }
            }
            int v = buf[(int)(pos - bufPos)] & 0xff;
            pos++;
            return v;
        }

        long u(int bytes) throws IOException {
            long v = 0;
            for (int i = 0; i < bytes; i++) v = (v << 8) | u8();
            return v;
        }

        int u16() throws IOException { return (int)u(2); }
        long u32() throws IOException { return u(4); }
        long u64() throws IOException { return u(8); }

        byte[] bytes(int n) throws IOException {
            byte[] b = new byte[n];
            int done = 0;
            while (done < n) {
                if (bufPos < 0 || pos < bufPos || pos >= bufPos + bufLen) {
                    if (pos >= len) throw new EOFException("unexpected end of file at " + pos);
                    f.seek(pos);
                    bufPos = pos;
                    bufLen = f.read(buf, 0, buf.length);
                    if (bufLen <= 0) {
                        bufPos = -1;
                        throw new EOFException("read failed at " + pos);
                    }
                }
                int avail = Math.min(n - done, (int)(bufPos + bufLen - pos));
                System.arraycopy(buf, (int)(pos - bufPos), b, done, avail);
                pos += avail;
                done += avail;
            }
            return b;
        }

        public void close() throws IOException { f.close(); }
    }
}
