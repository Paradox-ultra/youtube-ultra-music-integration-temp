package paradox.music;

import android.content.Context;
import org.json.*;
import paradox.dl.Account;
import java.io.*;
import java.net.*;
import java.util.*;

public final class MusicBrowseRequest {
    public static final String ENDPOINT = "https://music.youtube.com/youtubei/v1/browse?prettyPrint=false";
    public static final String BROWSE_EXPLORE = "FEmusic_explore";
    public static final String BROWSE_CHARTS = "FEmusic_charts";
    public static final String BROWSE_NEW_RELEASES = "FEmusic_new_releases";
    public static final String BROWSE_LIBRARY = "FEmusic_library_landing";
    public static final String BROWSE_LIKED = "FEmusic_liked_playlists";

    public static final class MusicItem {
        public String shelf = "";
        public String title = "";
        public String subtitle = "";
        public String videoId = "";
        public String playlistId = "";
        public String browseId = "";
        public String thumbnailUrl = "";
        public String duration = "";
        public String pageType = "";
    }

    public static final class BrowseResult {
        public String title = "";
        public String subtitle = "";
        public String thumbnailUrl = "";
        public String continuationToken = null;
        public final List<MusicItem> items = new ArrayList<>();
    }

    public static List<MusicItem> browse(Context context, String browseId, boolean authenticated) throws IOException, JSONException {
        return browseResult(context, browseId, authenticated).items;
    }

    public static BrowseResult browseResult(Context context, String browseId, boolean authenticated) throws IOException, JSONException {
        JSONObject payload = new JSONObject();
        JSONObject ctx = new JSONObject();
        JSONObject client = new JSONObject();
        client.put("clientName", "WEB_REMIX");
        client.put("clientVersion", "1.20231120.01.00");
        client.put("hl", "en");
        client.put("gl", "US");
        ctx.put("client", client);
        payload.put("context", ctx);
        payload.put("browseId", browseId);

        return executeRequest(ENDPOINT, payload, authenticated);
    }

    public static BrowseResult continuationResult(Context context, String continuationToken, boolean authenticated) throws IOException, JSONException {
        if (continuationToken == null || continuationToken.isEmpty()) {
            return new BrowseResult();
        }
        JSONObject payload = new JSONObject();
        JSONObject ctx = new JSONObject();
        JSONObject client = new JSONObject();
        client.put("clientName", "WEB_REMIX");
        client.put("clientVersion", "1.20231120.01.00");
        client.put("hl", "en");
        client.put("gl", "US");
        ctx.put("client", client);
        payload.put("context", ctx);
        payload.put("continuation", continuationToken);

        String url = ENDPOINT + "&continuation=" + URLEncoder.encode(continuationToken, "UTF-8");
        return executeRequest(url, payload, authenticated);
    }

    private static BrowseResult executeRequest(String urlStr, JSONObject payload, boolean authenticated) throws IOException, JSONException {
        Map<String, String> authHeaders = null;
        if (authenticated) {
            authHeaders = Account.headers();
            if (authHeaders == null || !authHeaders.containsKey("Authorization")) {
                throw new SecurityException("No active Google account session found in YouTube Ultra");
            }
        }

        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setConnectTimeout(12000);
        conn.setReadTimeout(15000);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // Required InnerTube WebRemix headers
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        conn.setRequestProperty("X-YouTube-Client-Name", "67");
        conn.setRequestProperty("X-YouTube-Client-Version", "1.20231120.01.00");
        conn.setRequestProperty("Origin", "https://music.youtube.com");

        if (authHeaders != null) {
            for (Map.Entry<String, String> e : authHeaders.entrySet()) {
                conn.setRequestProperty(e.getKey(), e.getValue());
            }
        }

        byte[] bodyBytes = payload.toString().getBytes("UTF-8");
        conn.setFixedLengthStreamingMode(bodyBytes.length);
        try (OutputStream out = conn.getOutputStream()) {
            out.write(bodyBytes);
        }

        int code = conn.getResponseCode();
        if (code != 200) {
            conn.disconnect();
            throw new IOException("Music browse request failed with HTTP " + code);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } finally {
            conn.disconnect();
        }

        return parseBrowseResponse(new JSONObject(response.toString()));
    }

    public static BrowseResult parseBrowseResponse(JSONObject json) {
        BrowseResult result = new BrowseResult();
        if (json == null) return result;

        // 1. Parse header (for album / playlist drilldown)
        JSONObject header = json.optJSONObject("header");
        if (header != null) {
            JSONObject respHeader = header.optJSONObject("musicResponsiveHeaderRenderer");
            if (respHeader == null) respHeader = header.optJSONObject("musicDetailHeaderRenderer");
            if (respHeader == null) respHeader = header.optJSONObject("musicHeaderRenderer");
            if (respHeader != null) {
                result.title = extractRunsText(respHeader.optJSONObject("title"));
                StringBuilder sb = new StringBuilder();
                String s1 = extractRunsText(respHeader.optJSONObject("subtitle"));
                String s2 = extractRunsText(respHeader.optJSONObject("secondSubtitle"));
                if (!s1.isEmpty()) sb.append(s1);
                if (!s2.isEmpty()) { if (sb.length() > 0) sb.append(" · "); sb.append(s2); }
                result.subtitle = sb.toString();
                result.thumbnailUrl = extractThumbnailUrl(respHeader);
            }
        }

        // 2. Check continuationContents (for pagination responses)
        JSONObject contContents = json.optJSONObject("continuationContents");
        if (contContents != null) {
            parseContinuationContents(contContents, result);
            return result;
        }

        // 3. Collect section list contents across all known root structures
        List<JSONObject> sections = new ArrayList<>();

        // Case A: Root "contents" is a JSONArray (e.g. community_playlist.json or home_daily.json shelf)
        JSONArray rootContentsArr = json.optJSONArray("contents");
        if (rootContentsArr != null) {
            if (rootContentsArr.length() > 0 && (rootContentsArr.optJSONObject(0).has("musicTwoRowItemRenderer") || rootContentsArr.optJSONObject(0).has("musicResponsiveListItemRenderer"))) {
                parseSection(json, result);
            } else {
                for (int i = 0; i < rootContentsArr.length(); i++) {
                    JSONObject sec = rootContentsArr.optJSONObject(i);
                    if (sec != null) sections.add(sec);
                }
            }
        }

        // Case B: Root "contents" is a JSONObject
        JSONObject rootContentsObj = json.optJSONObject("contents");
        if (rootContentsObj != null) {
            // singleColumnBrowseResultsRenderer
            JSONObject singleCol = rootContentsObj.optJSONObject("singleColumnBrowseResultsRenderer");
            if (singleCol != null) {
                extractSectionsFromTabs(singleCol.optJSONArray("tabs"), sections, result);
            }
            // twoColumnBrowseResultsRenderer
            JSONObject twoCol = rootContentsObj.optJSONObject("twoColumnBrowseResultsRenderer");
            if (twoCol != null) {
                JSONObject secContents = twoCol.optJSONObject("secondaryContents");
                if (secContents != null) {
                    JSONObject secList = secContents.optJSONObject("sectionListRenderer");
                    if (secList != null) {
                        extractContinuation(secList, result);
                        appendJsonArray(secList.optJSONArray("contents"), sections);
                    }
                }
                extractSectionsFromTabs(twoCol.optJSONArray("tabs"), sections, result);
            }
            // Direct sectionListRenderer under contents
            JSONObject secList = rootContentsObj.optJSONObject("sectionListRenderer");
            if (secList != null) {
                extractContinuation(secList, result);
                appendJsonArray(secList.optJSONArray("contents"), sections);
            }
            // Direct musicPlaylistShelfRenderer / musicShelfRenderer under contents
            if (rootContentsObj.has("musicPlaylistShelfRenderer") || rootContentsObj.has("musicShelfRenderer")) {
                sections.add(rootContentsObj);
            }
        }

        // 4. Parse all collected sections and their items
        for (JSONObject sec : sections) {
            parseSection(sec, result);
        }

        return result;
    }

    private static void extractSectionsFromTabs(JSONArray tabs, List<JSONObject> sections, BrowseResult result) {
        if (tabs == null) return;
        for (int t = 0; t < tabs.length(); t++) {
            JSONObject tabObj = tabs.optJSONObject(t);
            if (tabObj == null) continue;
            JSONObject tabRenderer = tabObj.optJSONObject("tabRenderer");
            if (tabRenderer == null) continue;
            JSONObject tabContent = tabRenderer.optJSONObject("content");
            if (tabContent == null) continue;
            JSONObject sectionList = tabContent.optJSONObject("sectionListRenderer");
            if (sectionList == null) continue;
            extractContinuation(sectionList, result);
            appendJsonArray(sectionList.optJSONArray("contents"), sections);
        }
    }

    private static void parseSection(JSONObject sec, BrowseResult result) {
        if (sec == null) return;

        if (sec.has("musicResponsiveListItemRenderer")) {
            MusicItem item = parseResponsiveListItem(sec.optJSONObject("musicResponsiveListItemRenderer"), "Music");
            if (item != null) result.items.add(item);
            return;
        }
        if (sec.has("musicTwoRowItemRenderer")) {
            MusicItem item = parseTwoRowItem(sec.optJSONObject("musicTwoRowItemRenderer"), "Music");
            if (item != null) result.items.add(item);
            return;
        }

        JSONObject shelf = sec.optJSONObject("musicCarouselShelfRenderer");
        if (shelf == null) shelf = sec.optJSONObject("musicShelfRenderer");
        if (shelf == null) shelf = sec.optJSONObject("musicPlaylistShelfRenderer");
        if (shelf == null) shelf = sec; // In case the shelf is direct

        String shelfTitle = "Music";
        JSONObject header = shelf.optJSONObject("header");
        if (header != null) {
            JSONObject basicHeader = header.optJSONObject("musicCarouselShelfBasicHeaderRenderer");
            if (basicHeader != null) {
                shelfTitle = extractRunsText(basicHeader.optJSONObject("title"));
            } else {
                JSONObject hdrRenderer = header.optJSONObject("musicHeaderRenderer");
                if (hdrRenderer != null) {
                    shelfTitle = extractRunsText(hdrRenderer.optJSONObject("title"));
                }
            }
        } else if (shelf.has("title")) {
            shelfTitle = extractRunsText(shelf.optJSONObject("title"));
        }
        if (shelfTitle.isEmpty()) shelfTitle = "Music";

        // Check for continuation at shelf level
        extractContinuation(shelf, result);

        JSONArray contents = shelf.optJSONArray("contents");
        if (contents == null) return;

        for (int c = 0; c < contents.length(); c++) {
            JSONObject itObj = contents.optJSONObject(c);
            if (itObj == null) continue;

            JSONObject responsive = itObj.optJSONObject("musicResponsiveListItemRenderer");
            if (responsive != null) {
                MusicItem item = parseResponsiveListItem(responsive, shelfTitle);
                if (item != null) result.items.add(item);
                continue;
            }

            JSONObject twoRow = itObj.optJSONObject("musicTwoRowItemRenderer");
            if (twoRow != null) {
                MusicItem item = parseTwoRowItem(twoRow, shelfTitle);
                if (item != null) result.items.add(item);
            }
        }
    }

    private static void parseContinuationContents(JSONObject contContents, BrowseResult result) {
        JSONObject playlistCont = contContents.optJSONObject("musicPlaylistShelfContinuation");
        if (playlistCont != null) {
            extractContinuation(playlistCont, result);
            parseShelfItems(playlistCont.optJSONArray("contents"), "Playlist", result);
            return;
        }

        JSONObject shelfCont = contContents.optJSONObject("musicShelfContinuation");
        if (shelfCont != null) {
            extractContinuation(shelfCont, result);
            parseShelfItems(shelfCont.optJSONArray("contents"), "Music", result);
            return;
        }

        JSONObject secListCont = contContents.optJSONObject("sectionListContinuation");
        if (secListCont != null) {
            extractContinuation(secListCont, result);
            JSONArray sections = secListCont.optJSONArray("contents");
            if (sections != null) {
                for (int i = 0; i < sections.length(); i++) {
                    parseSection(sections.optJSONObject(i), result);
                }
            }
        }
    }

    private static void parseShelfItems(JSONArray contents, String shelfTitle, BrowseResult result) {
        if (contents == null) return;
        for (int i = 0; i < contents.length(); i++) {
            JSONObject obj = contents.optJSONObject(i);
            if (obj == null) continue;
            JSONObject responsive = obj.optJSONObject("musicResponsiveListItemRenderer");
            if (responsive != null) {
                MusicItem item = parseResponsiveListItem(responsive, shelfTitle);
                if (item != null) result.items.add(item);
            }
        }
    }

    public static MusicItem parseResponsiveListItem(JSONObject renderer, String shelfTitle) {
        if (renderer == null) return null;
        MusicItem item = new MusicItem();
        item.shelf = shelfTitle;

        // Grounded parsing for real InnerTube musicResponsiveListItemRenderer flexColumns:
        // flexColumns[0] = Track Title and watch/browse navigationEndpoint
        // flexColumns[1] = Artists, Album, Year, Subtitle
        // flexColumns[2] = Duration (or subsequent column)
        JSONArray flexCols = renderer.optJSONArray("flexColumns");
        if (flexCols != null && flexCols.length() > 0) {
            // Column 0: Title and Navigation
            JSONObject col0 = flexCols.optJSONObject(0);
            if (col0 != null) {
                JSONObject flex0 = col0.optJSONObject("musicResponsiveListItemFlexColumnRenderer");
                if (flex0 != null) {
                    JSONObject textObj = flex0.optJSONObject("text");
                    if (textObj != null) {
                        item.title = extractRunsText(textObj);
                        JSONArray runs = textObj.optJSONArray("runs");
                        if (runs != null && runs.length() > 0) {
                            extractEndpoint(runs.optJSONObject(0).optJSONObject("navigationEndpoint"), item);
                        }
                    }
                }
            }

            // Column 1: Subtitle (Artists, Album)
            if (flexCols.length() > 1) {
                JSONObject col1 = flexCols.optJSONObject(1);
                if (col1 != null) {
                    JSONObject flex1 = col1.optJSONObject("musicResponsiveListItemFlexColumnRenderer");
                    if (flex1 != null) {
                        JSONObject textObj = flex1.optJSONObject("text");
                        if (textObj != null) {
                            item.subtitle = extractRunsText(textObj);
                            // If primary browseId not set from title, check subtitle artist browseEndpoint
                            if (item.browseId.isEmpty()) {
                                JSONArray runs = textObj.optJSONArray("runs");
                                if (runs != null && runs.length() > 0) {
                                    extractEndpoint(runs.optJSONObject(0).optJSONObject("navigationEndpoint"), item);
                                }
                            }
                        }
                    }
                }
            }

            // Column 2+: Duration or additional metadata
            for (int k = 2; k < flexCols.length(); k++) {
                JSONObject colK = flexCols.optJSONObject(k);
                if (colK == null) continue;
                JSONObject flexK = colK.optJSONObject("musicResponsiveListItemFlexColumnRenderer");
                if (flexK == null) continue;
                String txt = extractRunsText(flexK.optJSONObject("text"));
                if (txt.matches("^\\d+:\\d+$")) {
                    item.duration = txt;
                    break;
                }
            }
        }

        // Fallbacks for flat renderers without flexColumns
        if (item.title.isEmpty()) {
            item.title = extractRunsText(renderer.optJSONObject("title"));
        }
        if (item.subtitle.isEmpty()) {
            item.subtitle = extractRunsText(renderer.optJSONObject("subtitle"));
        }

        // Fallbacks for Video ID
        if (item.videoId.isEmpty()) {
            JSONObject data = renderer.optJSONObject("playlistItemData");
            if (data != null) {
                item.videoId = data.optString("videoId", "");
            }
        }
        if (item.videoId.isEmpty()) {
            extractEndpoint(renderer.optJSONObject("navigationEndpoint"), item);
        }
        if (item.videoId.isEmpty()) {
            extractEndpoint(renderer.optJSONObject("playNavigationEndpoint"), item);
        }
        if (item.videoId.isEmpty()) {
            JSONObject overlay = renderer.optJSONObject("overlay");
            if (overlay != null) {
                JSONObject thumbOverlay = overlay.optJSONObject("musicItemThumbnailOverlayRenderer");
                if (thumbOverlay != null) {
                    JSONObject content = thumbOverlay.optJSONObject("content");
                    if (content != null) {
                        JSONObject playBtn = content.optJSONObject("musicPlayButtonRenderer");
                        if (playBtn != null) {
                            extractEndpoint(playBtn.optJSONObject("playNavigationEndpoint"), item);
                        }
                    }
                }
            }
        }

        // Thumbnail
        item.thumbnailUrl = extractThumbnailUrl(renderer);

        return !item.title.isEmpty() ? item : null;
    }

    public static MusicItem parseTwoRowItem(JSONObject renderer, String shelfTitle) {
        if (renderer == null) return null;
        MusicItem item = new MusicItem();
        item.shelf = shelfTitle;

        item.title = extractRunsText(renderer.optJSONObject("title"));
        item.subtitle = extractRunsText(renderer.optJSONObject("subtitle"));
        extractEndpoint(renderer.optJSONObject("navigationEndpoint"), item);
        item.thumbnailUrl = extractThumbnailUrl(renderer);

        return !item.title.isEmpty() ? item : null;
    }

    private static void extractEndpoint(JSONObject nav, MusicItem item) {
        if (nav == null) return;
        JSONObject watch = nav.optJSONObject("watchEndpoint");
        if (watch != null) {
            String vid = watch.optString("videoId", "");
            if (!vid.isEmpty()) item.videoId = vid;
            String pid = watch.optString("playlistId", "");
            if (!pid.isEmpty()) item.playlistId = pid;
        }
        JSONObject browse = nav.optJSONObject("browseEndpoint");
        if (browse != null) {
            String bid = browse.optString("browseId", "");
            if (!bid.isEmpty()) {
                item.browseId = bid;
                if (item.playlistId.isEmpty() && bid.startsWith("VL")) {
                    item.playlistId = bid.substring(2);
                }
            }
            JSONObject configs = browse.optJSONObject("browseEndpointContextSupportedConfigs");
            if (configs != null) {
                JSONObject musicConfig = configs.optJSONObject("browseEndpointContextMusicConfig");
                if (musicConfig != null) {
                    item.pageType = musicConfig.optString("pageType", "");
                }
            }
        }
    }

    private static String extractRunsText(JSONObject textObj) {
        if (textObj == null) return "";
        JSONArray runs = textObj.optJSONArray("runs");
        if (runs != null && runs.length() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < runs.length(); i++) {
                JSONObject r = runs.optJSONObject(i);
                if (r != null) {
                    sb.append(r.optString("text", ""));
                }
            }
            return sb.toString().trim();
        }
        return textObj.optString("simpleText", "").trim();
    }

    private static String extractThumbnailUrl(JSONObject obj) {
        if (obj == null) return "";
        JSONObject thumbRenderer = obj.optJSONObject("thumbnailRenderer");
        if (thumbRenderer == null) thumbRenderer = obj.optJSONObject("thumbnail");
        if (thumbRenderer != null) {
            JSONObject musicThumb = thumbRenderer.optJSONObject("musicThumbnailRenderer");
            if (musicThumb != null) thumbRenderer = musicThumb;
            JSONObject thumb = thumbRenderer.optJSONObject("thumbnail");
            if (thumb != null) {
                JSONArray thumbs = thumb.optJSONArray("thumbnails");
                if (thumbs != null && thumbs.length() > 0) {
                    return thumbs.optJSONObject(thumbs.length() - 1).optString("url", "");
                }
            }
        }
        return "";
    }

    private static void extractContinuation(JSONObject obj, BrowseResult result) {
        if (obj == null || result.continuationToken != null) return;
        JSONArray conts = obj.optJSONArray("continuations");
        if (conts != null && conts.length() > 0) {
            JSONObject cont = conts.optJSONObject(0);
            if (cont != null) {
                JSONObject next = cont.optJSONObject("nextContinuationData");
                if (next != null) {
                    result.continuationToken = next.optString("continuation", null);
                }
            }
        }
    }

    private static void appendJsonArray(JSONArray src, List<JSONObject> dst) {
        if (src == null) return;
        for (int i = 0; i < src.length(); i++) {
            JSONObject o = src.optJSONObject(i);
            if (o != null) dst.add(o);
        }
    }
}
