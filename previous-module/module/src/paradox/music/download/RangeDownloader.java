package paradox.music.download;

/*
 * Copyright (C) Clash-Projects / LastWave contributors
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 *
 * Adapted from LastWave native TrackDownloadManagerStreaming.kt.
 * Multi-range parallel HTTP streaming:
 * - Sequential 512 KB range chunks (YOUTUBE_RANGE_BYTES)
 * - Up to 4 parallel parts in flight (PARALLEL_PARTS)
 * - Exact-offset writing via RandomAccessFile
 * - Retries each range from the exact byte reached
 * - Strict length verification, denominator total validation, and fd.sync()
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.*;

public final class RangeDownloader {
    public static final int YOUTUBE_RANGE_BYTES = 512 * 1024;
    public static final int PARALLEL_PARTS = 4;
    private static final int CONNECT_TIMEOUT_MS = 15000;
    private static final int READ_TIMEOUT_MS = 20000;
    private static final Pattern CONTENT_RANGE_PATTERN = Pattern.compile("(?i)^bytes\\s+(\\d+)-(\\d+)/(\\d+)$");

    public interface ProgressListener {
        void onProgress(long downloadedBytes, long totalBytes);
    }

    public interface CancelCheck {
        boolean isCancelled();
    }

    /**
     * Safely probes the server for the exact stream length using clen query parameter
     * or a single Range: bytes=0-0 GET probe. Never fabricates an arbitrary length.
     */
    public static long probeContentLength(String url, Map<String, String> headers) throws IOException {
        if (url == null || url.trim().isEmpty()) {
            throw new IOException("Cannot probe content length: missing or empty URL");
        }
        // 1. Check clen in URL query parameters
        int clenIdx = url.indexOf("clen=");
        if (clenIdx >= 0) {
            int end = url.indexOf('&', clenIdx);
            String val = url.substring(clenIdx + 5, end < 0 ? url.length() : end);
            try {
                long clen = Long.parseLong(val);
                if (clen > 0) return clen;
            } catch (NumberFormatException ignore) {}
        }

        // 2. HTTP probe using Range: bytes=0-0
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
        conn.setReadTimeout(READ_TIMEOUT_MS);
        conn.setUseCaches(false);
        conn.setRequestMethod("GET");
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                conn.setRequestProperty(e.getKey(), e.getValue());
            }
        }
        conn.setRequestProperty("Range", "bytes=0-0");

        try {
            int code = conn.getResponseCode();
            if (code == 206) {
                String cr = conn.getHeaderField("Content-Range");
                if (cr != null && !cr.trim().isEmpty()) {
                    Matcher m = CONTENT_RANGE_PATTERN.matcher(cr.trim());
                    if (m.matches()) {
                        long total = Long.parseLong(m.group(3));
                        if (total > 0) return total;
                    }
                }
            } else if (code == 200) {
                long cl = conn.getContentLengthLong();
                if (cl > 0) return cl;
            }
        } finally {
            conn.disconnect();
        }

        throw new IOException("Unable to discover stream content length; server did not provide valid Content-Range total");
    }

    public static boolean download(String url, File target, Map<String, String> headers, long totalLength, ProgressListener progress) throws IOException {
        return download(url, target, headers, totalLength, progress, null);
    }

    public static boolean download(String url, File target, Map<String, String> headers, long totalLength, ProgressListener progress, CancelCheck cancelCheck) throws IOException {
        if (url == null || target == null || totalLength <= 0) return false;
        if (cancelCheck != null && cancelCheck.isCancelled()) {
            throw new IOException("Download cancelled before start");
        }

        File parent = target.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        // 1. Preallocate file
        try (RandomAccessFile raf = new RandomAccessFile(target, "rw")) {
            raf.setLength(totalLength);
        }

        // 2. Generate 512KB ranges
        List<long[]> ranges = new ArrayList<>();
        for (long start = 0; start < totalLength; start += YOUTUBE_RANGE_BYTES) {
            long end = Math.min(totalLength - 1, start + YOUTUBE_RANGE_BYTES - 1);
            ranges.add(new long[]{start, end});
        }

        AtomicLong downloadedBytes = new AtomicLong(0);
        ExecutorService pool = Executors.newFixedThreadPool(PARALLEL_PARTS);
        List<Future<Void>> futures = new ArrayList<>();

        try {
            for (long[] r : ranges) {
                final long rangeStart = r[0];
                final long rangeEnd = r[1];
                futures.add(pool.submit(() -> {
                    downloadRangeWithRetry(url, target, headers, rangeStart, rangeEnd, totalLength, downloadedBytes, progress, cancelCheck);
                    return null;
                }));
            }

            for (Future<Void> f : futures) {
                f.get();
            }
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) throw (IOException) cause;
            throw new IOException("Range download failed: " + cause.getMessage(), cause);
        } catch (InterruptedException e) {
            throw new IOException("Download interrupted", e);
        } finally {
            pool.shutdownNow();
        }

        if (cancelCheck != null && cancelCheck.isCancelled()) {
            throw new IOException("Download cancelled");
        }

        // 3. Exact assertions and sync
        long finalLen = downloadedBytes.get();
        if (finalLen != totalLength || target.length() != totalLength) {
            throw new IOException("Range download truncated: received " + finalLen + " of " + totalLength + " bytes");
        }

        try (RandomAccessFile raf = new RandomAccessFile(target, "rw")) {
            raf.getFD().sync();
        }

        return true;
    }

    private static void downloadRangeWithRetry(String url, File target, Map<String, String> headers,
                                               long start, long end, long totalLength,
                                               AtomicLong downloadedBytes, ProgressListener progress,
                                               CancelCheck cancelCheck) throws IOException {
        int maxAttempts = 3;
        long[] currentOffset = new long[]{start};

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            if (cancelCheck != null && cancelCheck.isCancelled()) {
                throw new IOException("Download cancelled");
            }
            try {
                downloadRangeChunk(url, target, headers, currentOffset, end, totalLength, downloadedBytes, progress, cancelCheck);
                return;
            } catch (IOException e) {
                if (cancelCheck != null && cancelCheck.isCancelled()) {
                    throw e;
                }
                if (attempt == maxAttempts) throw e;
                try {
                    Thread.sleep(150L * attempt);
                } catch (InterruptedException ie) {
                    throw new IOException("Interrupted during retry backoff", ie);
                }
            }
        }
    }

    private static void downloadRangeChunk(String url, File target, Map<String, String> headers,
                                           long[] currentOffset, long end, long totalLength,
                                           AtomicLong downloadedBytes, ProgressListener progress,
                                           CancelCheck cancelCheck) throws IOException {
        long reqStart = currentOffset[0];
        if (reqStart > end) return; // already completed

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
        conn.setReadTimeout(READ_TIMEOUT_MS);
        conn.setUseCaches(false);
        conn.setRequestMethod("GET");

        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                conn.setRequestProperty(e.getKey(), e.getValue());
            }
        }
        conn.setRequestProperty("Range", "bytes=" + reqStart + "-" + end);

        int code = conn.getResponseCode();
        if (code == 200) {
            conn.disconnect();
            throw new IOException("Server ignored Range header and returned HTTP 200 OK instead of HTTP 206 Partial Content");
        }
        if (code != 206) {
            conn.disconnect();
            throw new IOException("HTTP " + code + " for range " + reqStart + "-" + end);
        }

        // Validate Content-Range header
        String cr = conn.getHeaderField("Content-Range");
        if (cr == null || cr.trim().isEmpty()) {
            conn.disconnect();
            throw new IOException("Missing Content-Range header in HTTP 206 response for range " + reqStart + "-" + end);
        }

        Matcher m = CONTENT_RANGE_PATTERN.matcher(cr.trim());
        if (!m.matches()) {
            conn.disconnect();
            throw new IOException("Malformed Content-Range header: '" + cr + "'");
        }

        long crStart = Long.parseLong(m.group(1));
        long crEnd = Long.parseLong(m.group(2));
        long crTotal = Long.parseLong(m.group(3));

        if (crStart != reqStart || crEnd != end) {
            conn.disconnect();
            throw new IOException("Mismatched Content-Range header: got bytes " + crStart + "-" + crEnd + ", expected " + reqStart + "-" + end);
        }

        if (totalLength > 0 && crTotal != totalLength) {
            conn.disconnect();
            throw new IOException("Mismatched Content-Range total: got " + crTotal + ", expected " + totalLength);
        }

        try (InputStream in = conn.getInputStream();
             RandomAccessFile raf = new RandomAccessFile(target, "rw")) {
            raf.seek(reqStart);
            byte[] buf = new byte[8192];
            long remaining = end - reqStart + 1;
            int n;
            while (remaining > 0 && (n = in.read(buf, 0, (int) Math.min(buf.length, remaining))) > 0) {
                if (cancelCheck != null && cancelCheck.isCancelled()) {
                    throw new IOException("Download cancelled");
                }
                raf.write(buf, 0, n);
                remaining -= n;
                currentOffset[0] += n;
                long totalSoFar = downloadedBytes.addAndGet(n);
                if (progress != null) {
                    progress.onProgress(totalSoFar, totalLength);
                }
            }
            if (remaining > 0) {
                throw new EOFException("Premature EOF in range " + reqStart + "-" + end + " at offset " + currentOffset[0]);
            }
        } finally {
            conn.disconnect();
        }
    }
}
