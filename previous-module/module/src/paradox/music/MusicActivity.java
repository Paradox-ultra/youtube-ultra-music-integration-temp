package paradox.music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import paradox.dl.*;
import java.util.*;

public class MusicActivity extends Activity {
    public static boolean launch(Context c) {
        if (c == null) return false;
        try {
            Intent intent = new Intent(c, MusicActivity.class);
            if (!(c instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            c.startActivity(intent);
            return true;
        } catch (Throwable t) {
            android.util.Log.e("ParadoxMusic", "Failed to launch MusicActivity", t);
            return false;
        }
    }

    private Ui.Pal P;
    private int selectedTab = 0; // 0 = Library (Default/Home), 1 = Explore
    private FrameLayout contentContainer;
    private TextView tabLibraryText;
    private TextView tabExploreText;
    private View tabLibraryIndicator;
    private View tabExploreIndicator;
    private Map<String, String> currentAccountHeaders;
    private List<MusicBrowseRequest.MusicItem> cachedExploreItems = null;
    private List<MusicBrowseRequest.MusicItem> cachedLibraryItems = null;
    private final java.util.concurrent.atomic.AtomicInteger accountGeneration = new java.util.concurrent.atomic.AtomicInteger(0);
    private String currentDrilldownBrowseId = null;

    @Override
    public void onBackPressed() {
        if (currentDrilldownBrowseId != null) {
            currentDrilldownBrowseId = null;
            renderTab();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        P = Ui.pal(this);
        setTheme(P.dark ? android.R.style.Theme_Material_NoActionBar : android.R.style.Theme_Material_Light_NoActionBar);
        super.onCreate(savedInstanceState);

        currentAccountHeaders = Account.headers();

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(P.bg);

        // Top Toolbar
        root.addView(createToolbar());

        // Selective Two-Tab Bar: Library (Home) & Explore (Omit Home & Samples)
        root.addView(createTabBar());

        // Content Area
        contentContainer = new FrameLayout(this);
        root.addView(contentContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));

        setContentView(root);
        Ui.themed(this);

        renderTab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Ui.dark(this) != P.dark) {
            recreate();
            return;
        }
        // Account Switch & Logout Invalidation
        Map<String, String> newHeaders = Account.headers();
        if (!Objects.equals(newHeaders, currentAccountHeaders)) {
            currentAccountHeaders = newHeaders;
            accountGeneration.incrementAndGet();
            cachedLibraryItems = null; // Invalidate cache on account change/logout
            cachedExploreItems = null; // Clear BOTH Explore and Library!
            renderTab();
        } else if (selectedTab == 0) {
            renderTab(); // Refresh local downloads
        }
    }

    private View createToolbar() {
        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setBackgroundColor(P.sheet);
        bar.setPadding(Ui.dp(this, 8), Ui.dp(this, 8), Ui.dp(this, 12), Ui.dp(this, 8));

        // Back button
        ImageButton back = new ImageButton(this);
        back.setBackground(null);
        back.setImageDrawable(new Ui.Icon(Ui.I_BACK, P.text));
        back.setContentDescription("Back");
        back.setOnClickListener(v -> finish());
        bar.addView(back, new LinearLayout.LayoutParams(Ui.dp(this, 40), Ui.dp(this, 40)));

        // Title
        TextView title = new TextView(this);
        title.setText("Music");
        title.setTextColor(P.text);
        title.setTextSize(20);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setPadding(Ui.dp(this, 8), 0, 0, 0);
        bar.addView(title, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

        // Settings Button
        ImageButton settings = new ImageButton(this);
        settings.setBackground(null);
        settings.setImageDrawable(new Ui.Icon(Ui.I_MORE, P.text));
        settings.setContentDescription("Settings");
        settings.setOnClickListener(v -> {
            PopupMenu pm = new PopupMenu(this, v);
            pm.getMenu().add(0, 1, 0, "Music & Download Settings");
            pm.setOnMenuItemClickListener(mi -> {
                startActivity(new Intent(this, DownloadSettingsActivity.class));
                return true;
            });
            pm.show();
        });
        bar.addView(settings, new LinearLayout.LayoutParams(Ui.dp(this, 40), Ui.dp(this, 40)));

        return bar;
    }

    private View createTabBar() {
        LinearLayout tabBar = new LinearLayout(this);
        tabBar.setOrientation(LinearLayout.HORIZONTAL);
        tabBar.setBackgroundColor(P.sheet);

        // Tab 1: Library
        LinearLayout tab1 = new LinearLayout(this);
        tab1.setOrientation(LinearLayout.VERTICAL);
        tab1.setGravity(Gravity.CENTER);
        tab1.setClickable(true);
        tab1.setPadding(0, Ui.dp(this, 10), 0, 0);

        tabLibraryText = new TextView(this);
        tabLibraryText.setText("Library");
        tabLibraryText.setTextSize(14);
        tabLibraryText.setTypeface(Typeface.DEFAULT_BOLD);
        tab1.addView(tabLibraryText);

        tabLibraryIndicator = new View(this);
        LinearLayout.LayoutParams indParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Ui.dp(this, 3));
        indParams1.topMargin = Ui.dp(this, 8);
        tab1.addView(tabLibraryIndicator, indParams1);

        tab1.setOnClickListener(v -> {
            if (selectedTab != 0) {
                selectedTab = 0;
                updateTabStyles();
                renderTab();
            }
        });
        tabBar.addView(tab1, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

        // Tab 2: Explore
        LinearLayout tab2 = new LinearLayout(this);
        tab2.setOrientation(LinearLayout.VERTICAL);
        tab2.setGravity(Gravity.CENTER);
        tab2.setClickable(true);
        tab2.setPadding(0, Ui.dp(this, 10), 0, 0);

        tabExploreText = new TextView(this);
        tabExploreText.setText("Explore");
        tabExploreText.setTextSize(14);
        tabExploreText.setTypeface(Typeface.DEFAULT_BOLD);
        tab2.addView(tabExploreText);

        tabExploreIndicator = new View(this);
        LinearLayout.LayoutParams indParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Ui.dp(this, 3));
        indParams2.topMargin = Ui.dp(this, 8);
        tab2.addView(tabExploreIndicator, indParams2);

        tab2.setOnClickListener(v -> {
            if (selectedTab != 1) {
                selectedTab = 1;
                updateTabStyles();
                renderTab();
            }
        });
        tabBar.addView(tab2, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

        updateTabStyles();
        return tabBar;
    }

    private void updateTabStyles() {
        tabLibraryText.setTextColor(selectedTab == 0 ? P.text : P.sub);
        tabLibraryIndicator.setBackgroundColor(selectedTab == 0 ? P.text : Color.TRANSPARENT);

        tabExploreText.setTextColor(selectedTab == 1 ? P.text : P.sub);
        tabExploreIndicator.setBackgroundColor(selectedTab == 1 ? P.text : Color.TRANSPARENT);
    }

    private void renderTab() {
        contentContainer.removeAllViews();
        if (selectedTab == 0) {
            renderLibrary();
        } else {
            renderExplore();
        }
    }

    // --- Tab 1: Library (Default / Home) ---
    private void renderLibrary() {
        ScrollView scroll = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(Ui.dp(this, 16), Ui.dp(this, 16), Ui.dp(this, 16), Ui.dp(this, 16));

        // Account status header
        TextView accountStatus = new TextView(this);
        if (Account.signedIn()) {
            accountStatus.setText("Google Account Active · Synced with YouTube Ultra");
            accountStatus.setTextColor(P.text);
        } else {
            accountStatus.setText("Signed Out · Local Offline Mode");
            accountStatus.setTextColor(P.sub);
        }
        accountStatus.setTextSize(12);
        accountStatus.setPadding(0, 0, 0, Ui.dp(this, 12));
        layout.addView(accountStatus);

        // Section: Downloaded Music (Local Offline)
        TextView secTitle = new TextView(this);
        secTitle.setText("Downloaded Music (Music/YouTube Ultra)");
        secTitle.setTextColor(P.text);
        secTitle.setTextSize(16);
        secTitle.setTypeface(Typeface.DEFAULT_BOLD);
        secTitle.setPadding(0, 0, 0, Ui.dp(this, 8));
        layout.addView(secTitle);

        List<Store.Item> allItems = Store.all(this);
        List<Store.Item> audioItems = new ArrayList<>();
        for (Store.Item it : allItems) {
            if (it.audio && "done".equals(it.status)) {
                audioItems.add(it);
            }
        }

        if (audioItems.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No local music found in Music/YouTube Ultra.\nDownloaded tracks from Explore or YouTube will appear here offline.");
            empty.setTextColor(P.sub);
            empty.setTextSize(13);
            empty.setPadding(0, Ui.dp(this, 4), 0, Ui.dp(this, 16));
            layout.addView(empty);
        } else {
            for (Store.Item it : audioItems) {
                layout.addView(createLocalAudioRow(it));
            }
        }

        // Adopt external files in background
        AsyncTask.execute(() -> {
            if (Store.adoptSaved(MusicActivity.this)) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (selectedTab == 0) renderLibrary();
                });
            }
        });

        // Cloud Library & Playlists Section
        TextView cloudTitle = new TextView(this);
        cloudTitle.setText("Cloud Playlists & Saved Library");
        cloudTitle.setTextColor(P.text);
        cloudTitle.setTextSize(16);
        cloudTitle.setTypeface(Typeface.DEFAULT_BOLD);
        cloudTitle.setPadding(0, Ui.dp(this, 20), 0, Ui.dp(this, 8));
        layout.addView(cloudTitle);

        if (!Account.signedIn()) {
            TextView signInHint = new TextView(this);
            signInHint.setText("Sign in to your Google Account in YouTube Ultra to sync your cloud music library, liked playlists, and saved albums.");
            signInHint.setTextColor(P.sub);
            signInHint.setTextSize(13);
            layout.addView(signInHint);
        } else if (cachedLibraryItems != null) {
            renderCloudLibraryItems(layout, cachedLibraryItems);
        } else {
            ProgressBar pb = new ProgressBar(this);
            layout.addView(pb);
            loadCloudLibrary(layout, pb);
        }

        scroll.addView(layout);
        contentContainer.addView(scroll);
    }

    private View createLocalAudioRow(Store.Item it) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, Ui.dp(this, 8), 0, Ui.dp(this, 8));

        ImageView icon = new ImageView(this);
        icon.setImageDrawable(new Ui.Icon(Ui.I_AUDIO, P.text));
        row.addView(icon, new LinearLayout.LayoutParams(Ui.dp(this, 28), Ui.dp(this, 28)));

        LinearLayout textCol = new LinearLayout(this);
        textCol.setOrientation(LinearLayout.VERTICAL);
        textCol.setPadding(Ui.dp(this, 12), 0, 0, 0);

        TextView title = new TextView(this);
        title.setText(it.title != null ? it.title : "Track");
        title.setTextColor(P.text);
        title.setTextSize(14);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setSingleLine(true);
        textCol.addView(title);

        TextView sub = new TextView(this);
        String label = (it.channel != null && !it.channel.isEmpty()) ? it.channel : "Offline Audio";
        if (it.total > 0) label += " · " + (it.total / (1024 * 1024)) + " MB";
        sub.setText(label);
        sub.setTextColor(P.sub);
        sub.setTextSize(12);
        textCol.addView(sub);

        row.addView(textCol, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

        row.setClickable(true);
        row.setOnClickListener(v -> Open.play(this, it, false));

        return row;
    }

    private void loadCloudLibrary(LinearLayout container, ProgressBar pb) {
        final int gen = accountGeneration.get();
        AsyncTask.execute(() -> {
            try {
                // Fetch real library playlists via MusicBrowseRequest using current active account headers
                List<MusicBrowseRequest.MusicItem> items = MusicBrowseRequest.browse(MusicActivity.this, MusicBrowseRequest.BROWSE_LIBRARY, true);
                if (items.isEmpty()) {
                    items = MusicBrowseRequest.browse(MusicActivity.this, MusicBrowseRequest.BROWSE_LIKED, true);
                }
                if (gen != accountGeneration.get()) return; // Stale account generation guard
                final List<MusicBrowseRequest.MusicItem> result = items;
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (gen != accountGeneration.get()) return; // UI callback guard
                    cachedLibraryItems = result;
                    container.removeView(pb);
                    renderCloudLibraryItems(container, result);
                });
            } catch (Throwable t) {
                if (gen != accountGeneration.get()) return;
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (gen != accountGeneration.get()) return;
                    container.removeView(pb);
                    TextView err = new TextView(MusicActivity.this);
                    err.setText("Unable to sync cloud library: " + t.getMessage());
                    err.setTextColor(P.sub);
                    err.setTextSize(13);
                    container.addView(err);
                });
            }
        });
    }

    private void renderCloudLibraryItems(LinearLayout container, List<MusicBrowseRequest.MusicItem> items) {
        if (items.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No saved playlists or albums found in your Google Music library.");
            empty.setTextColor(P.sub);
            empty.setTextSize(13);
            container.addView(empty);
            return;
        }

        String currentShelf = "";
        for (MusicBrowseRequest.MusicItem it : items) {
            if (!it.shelf.equals(currentShelf)) {
                currentShelf = it.shelf;
                TextView shelfHdr = new TextView(this);
                shelfHdr.setText(currentShelf);
                shelfHdr.setTextColor(P.text);
                shelfHdr.setTextSize(14);
                shelfHdr.setTypeface(Typeface.DEFAULT_BOLD);
                shelfHdr.setPadding(0, Ui.dp(this, 12), 0, Ui.dp(this, 4));
                container.addView(shelfHdr);
            }
            container.addView(createBrowsedItemRow(it));
        }
    }

    // --- Tab 2: Explore ---
    private void renderExplore() {
        ScrollView scroll = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(Ui.dp(this, 16), Ui.dp(this, 16), Ui.dp(this, 16), Ui.dp(this, 16));

        TextView title = new TextView(this);
        title.setText("Explore & Trending Music");
        title.setTextColor(P.text);
        title.setTextSize(18);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setPadding(0, 0, 0, Ui.dp(this, 8));
        layout.addView(title);

        if (cachedExploreItems != null) {
            renderExploreItems(layout, cachedExploreItems);
        } else {
            ProgressBar pb = new ProgressBar(this);
            layout.addView(pb);
            final int gen = accountGeneration.get();

            AsyncTask.execute(() -> {
                try {
                    List<MusicBrowseRequest.MusicItem> items = MusicBrowseRequest.browse(MusicActivity.this, MusicBrowseRequest.BROWSE_EXPLORE, false);
                    if (items.isEmpty()) {
                        items = MusicBrowseRequest.browse(MusicActivity.this, MusicBrowseRequest.BROWSE_CHARTS, false);
                    }
                    if (gen != accountGeneration.get()) return;
                    final List<MusicBrowseRequest.MusicItem> result = items;
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (gen != accountGeneration.get()) return;
                        cachedExploreItems = result;
                        layout.removeView(pb);
                        renderExploreItems(layout, result);
                    });
                } catch (Throwable t) {
                    if (gen != accountGeneration.get()) return;
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (gen != accountGeneration.get()) return;
                        layout.removeView(pb);
                        TextView err = new TextView(MusicActivity.this);
                        err.setText("Unable to load Explore content: " + t.getMessage());
                        err.setTextColor(P.sub);
                        err.setTextSize(13);
                        layout.addView(err);
                    });
                }
            });
        }

        scroll.addView(layout);
        contentContainer.addView(scroll);
    }

    private void renderExploreItems(LinearLayout container, List<MusicBrowseRequest.MusicItem> items) {
        if (items.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No explore items available right now.");
            empty.setTextColor(P.sub);
            empty.setTextSize(13);
            container.addView(empty);
            return;
        }

        String currentShelf = "";
        for (MusicBrowseRequest.MusicItem it : items) {
            if (!it.shelf.equals(currentShelf)) {
                currentShelf = it.shelf;
                TextView shelfHdr = new TextView(this);
                shelfHdr.setText(currentShelf);
                shelfHdr.setTextColor(P.text);
                shelfHdr.setTextSize(15);
                shelfHdr.setTypeface(Typeface.DEFAULT_BOLD);
                shelfHdr.setPadding(0, Ui.dp(this, 14), 0, Ui.dp(this, 6));
                container.addView(shelfHdr);
            }
            container.addView(createBrowsedItemRow(it));
        }
    }

    private View createBrowsedItemRow(MusicBrowseRequest.MusicItem it) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, Ui.dp(this, 8), 0, Ui.dp(this, 8));

        ImageView icon = new ImageView(this);
        icon.setImageDrawable(new Ui.Icon(Ui.I_AUDIO, P.text));
        row.addView(icon, new LinearLayout.LayoutParams(Ui.dp(this, 28), Ui.dp(this, 28)));

        LinearLayout textCol = new LinearLayout(this);
        textCol.setOrientation(LinearLayout.VERTICAL);
        textCol.setPadding(Ui.dp(this, 12), 0, Ui.dp(this, 8), 0);

        TextView title = new TextView(this);
        title.setText(it.title);
        title.setTextColor(P.text);
        title.setTextSize(14);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setSingleLine(true);
        textCol.addView(title);

        TextView sub = new TextView(this);
        sub.setText(it.subtitle.isEmpty() ? "YouTube Music" : it.subtitle);
        sub.setTextColor(P.sub);
        sub.setTextSize(12);
        sub.setSingleLine(true);
        textCol.addView(sub);

        row.addView(textCol, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

        // One-Click Download Button wired to DownloadActivity -> DownloadService
        if (!it.videoId.isEmpty()) {
            ImageButton dlBtn = new ImageButton(this);
            dlBtn.setBackground(null);
            dlBtn.setImageDrawable(new Ui.Icon(Ui.I_DOWNLOAD, P.text));
            dlBtn.setContentDescription("Download Track");
            dlBtn.setOnClickListener(v -> {
                Toast.makeText(this, "Starting music download: " + it.title, Toast.LENGTH_SHORT).show();
                DownloadActivity.startMusic(this, it.videoId, it.title, it.subtitle, it.thumbnailUrl);
            });
            row.addView(dlBtn, new LinearLayout.LayoutParams(Ui.dp(this, 36), Ui.dp(this, 36)));
        }

        row.setClickable(true);
        row.setOnClickListener(v -> {
            if (!it.videoId.isEmpty()) {
                // Open track via player
                try {
                    Intent playIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.youtube.com/watch?v=" + it.videoId));
                    playIntent.setPackage(getPackageName());
                    startActivity(playIntent);
                } catch (Throwable t) {
                    android.util.Log.w("ParadoxMusic", "Unable to launch player for " + it.videoId, t);
                    Toast.makeText(this, "Playback error: " + (t.getMessage() != null ? t.getMessage() : "player unavailable"), Toast.LENGTH_SHORT).show();
                }
            } else if (!it.browseId.isEmpty()) {
                drilldown(it.browseId, it.title);
            }
        });

        return row;
    }

    private void drilldown(final String browseId, final String title) {
        currentDrilldownBrowseId = browseId;
        contentContainer.removeAllViews();

        ScrollView scroll = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(Ui.dp(this, 16), Ui.dp(this, 16), Ui.dp(this, 16), Ui.dp(this, 16));

        LinearLayout hdrRow = new LinearLayout(this);
        hdrRow.setOrientation(LinearLayout.HORIZONTAL);
        hdrRow.setGravity(Gravity.CENTER_VERTICAL);
        hdrRow.setPadding(0, 0, 0, Ui.dp(this, 12));

        ImageButton backBtn = new ImageButton(this);
        backBtn.setBackground(null);
        backBtn.setImageDrawable(new Ui.Icon(Ui.I_BACK, P.text));
        backBtn.setOnClickListener(v -> {
            currentDrilldownBrowseId = null;
            renderTab();
        });
        hdrRow.addView(backBtn, new LinearLayout.LayoutParams(Ui.dp(this, 36), Ui.dp(this, 36)));

        TextView titleView = new TextView(this);
        titleView.setText(title != null && !title.isEmpty() ? title : "Album / Playlist");
        titleView.setTextColor(P.text);
        titleView.setTextSize(18);
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        titleView.setPadding(Ui.dp(this, 8), 0, 0, 0);
        hdrRow.addView(titleView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        layout.addView(hdrRow);

        ProgressBar pb = new ProgressBar(this);
        layout.addView(pb);

        scroll.addView(layout);
        contentContainer.addView(scroll);

        final int gen = accountGeneration.get();
        AsyncTask.execute(() -> {
            try {
                final MusicBrowseRequest.BrowseResult res = MusicBrowseRequest.browseResult(MusicActivity.this, browseId, Account.signedIn());
                if (gen != accountGeneration.get()) return;
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (gen != accountGeneration.get() || !Objects.equals(currentDrilldownBrowseId, browseId)) return;
                    layout.removeView(pb);
                    if (!res.subtitle.isEmpty()) {
                        TextView sub = new TextView(MusicActivity.this);
                        sub.setText(res.subtitle);
                        sub.setTextColor(P.sub);
                        sub.setTextSize(13);
                        sub.setPadding(0, 0, 0, Ui.dp(MusicActivity.this, 12));
                        layout.addView(sub);
                    }
                    if (res.items.isEmpty()) {
                        TextView empty = new TextView(MusicActivity.this);
                        empty.setText("No tracks found in this album or playlist.");
                        empty.setTextColor(P.sub);
                        empty.setTextSize(13);
                        layout.addView(empty);
                    } else {
                        for (MusicBrowseRequest.MusicItem it : res.items) {
                            layout.addView(createBrowsedItemRow(it));
                        }
                    }
                    if (res.continuationToken != null) {
                        Button loadMore = new Button(MusicActivity.this);
                        loadMore.setText("Load More Tracks");
                        loadMore.setTextColor(P.text);
                        loadMore.setOnClickListener(v -> loadContinuation(layout, loadMore, res.continuationToken));
                        layout.addView(loadMore);
                    }
                });
            } catch (Throwable t) {
                if (gen != accountGeneration.get()) return;
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (gen != accountGeneration.get() || !Objects.equals(currentDrilldownBrowseId, browseId)) return;
                    layout.removeView(pb);
                    TextView err = new TextView(MusicActivity.this);
                    err.setText("Unable to load: " + t.getMessage());
                    err.setTextColor(P.sub);
                    err.setTextSize(13);
                    layout.addView(err);
                });
            }
        });
    }

    private void loadContinuation(LinearLayout layout, Button loadMoreBtn, String continuationToken) {
        loadMoreBtn.setEnabled(false);
        loadMoreBtn.setText("Loading more…");
        final int gen = accountGeneration.get();
        AsyncTask.execute(() -> {
            try {
                final MusicBrowseRequest.BrowseResult res = MusicBrowseRequest.continuationResult(MusicActivity.this, continuationToken, Account.signedIn());
                if (gen != accountGeneration.get()) return;
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (gen != accountGeneration.get()) return;
                    layout.removeView(loadMoreBtn);
                    for (MusicBrowseRequest.MusicItem it : res.items) {
                        layout.addView(createBrowsedItemRow(it));
                    }
                    if (res.continuationToken != null) {
                        Button nextBtn = new Button(MusicActivity.this);
                        nextBtn.setText("Load More Tracks");
                        nextBtn.setTextColor(P.text);
                        nextBtn.setOnClickListener(v -> loadContinuation(layout, nextBtn, res.continuationToken));
                        layout.addView(nextBtn);
                    }
                });
            } catch (Throwable t) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    loadMoreBtn.setEnabled(true);
                    loadMoreBtn.setText("Retry Loading More");
                });
            }
        });
    }
}
