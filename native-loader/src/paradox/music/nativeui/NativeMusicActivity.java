package paradox.music.nativeui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.util.Log;
import android.widget.Toast;
import com.tencent.shadow.core.common.InstalledApk;
import com.tencent.shadow.core.common.Logger;
import com.tencent.shadow.core.common.LoggerFactory;
import com.tencent.shadow.core.load_parameters.LoadParameters;
import com.tencent.shadow.core.runtime.container.ContentProviderDelegateProviderHolder;
import com.tencent.shadow.core.runtime.container.DelegateProviderHolder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.util.Enumeration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class NativeMusicActivity extends Activity {
    private static final String TAG = "UltraMusic";
    private static final String ASSET_APK = "ultra_music/music.apk";
    private static final String PLUGIN_ACTIVITY = "com.google.android.apps.youtube.music.activities.MusicActivity";
    private static final Handler MAIN = new Handler(Looper.getMainLooper());
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "Ultra-Music-Loader");
        t.setDaemon(true);
        return t;
    });

    private static final Object LOCK = new Object();
    private static final AtomicBoolean PRELOAD_STARTED = new AtomicBoolean(false);
    private static final AtomicBoolean OPENING = new AtomicBoolean(false);

    private static MusicPluginLoader loader;
    private static boolean applicationCreated;
    private static boolean loggerReady;
    private static volatile String phase = "starting";

    /** Extraction-only preload. Does not load donor classes or run the donor Application. */
    public static void preload(Context context) {
        if (context == null) return;
        final Context appContext = context.getApplicationContext();
        NativeMusicDiagnostics.install(appContext);
        if (!PRELOAD_STARTED.compareAndSet(false, true)) return;
        EXECUTOR.execute(() -> {
            try {
                extractIfNeeded(appContext);
            } catch (Throwable e) {
                PRELOAD_STARTED.set(false);
                Log.e(TAG, "Background Music extraction preload failed at: " + phase, e);
            }
        });
    }

    /** Launches Music's native Library after the embedded plugin is ready. */
    public static void open(Context context) {
        if (context == null) return;
        final Context appContext = context.getApplicationContext();
        NativeMusicDiagnostics.install(appContext);
        if (!AccountBridge.hasSelectedHost()) {
            showToast(appContext, "Choose your account in YouTube first, then open Music");
            return;
        }
        if (!OPENING.compareAndSet(false, true)) return;
        NativeMusicDiagnostics.begin();
        EXECUTOR.execute(() -> {
            try {
                AccountBridge.requireSelectedHost();
                ExtractedPaths paths = extractIfNeeded(appContext);
                MusicPluginLoader readyLoader = ensurePluginLoaded(appContext, paths);
                ensureApplicationCreated(readyLoader);
                AccountBridge.synchronizeIfControllerBound();
                NativeMusicDiagnostics.install(appContext);

                phase = "Music activity launch";
                NativeMusicDiagnostics.phase(phase);
                MAIN.post(() -> {
                    try {
                        Context launchCtx = context;
                        boolean canUseActivity = launchCtx instanceof Activity
                            && !((Activity) launchCtx).isFinishing()
                            && (Build.VERSION.SDK_INT < 17 || !((Activity) launchCtx).isDestroyed());
                        if (!canUseActivity) launchCtx = appContext;
                        Intent intent = new Intent().setComponent(
                            new ComponentName(appContext.getPackageName(), PLUGIN_ACTIVITY));
                        Intent converted = readyLoader.getComponentManager().convertPluginActivityIntent(intent);
                        if (!(launchCtx instanceof Activity)) converted.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchCtx.startActivity(converted);
                    } catch (Throwable error) {
                        NativeMusicDiagnostics.showCaught(context, "Caught activity launch failure", error);
                        Log.e(TAG, "Music activity launch failed", error);
                    } finally {
                        OPENING.set(false);
                    }
                });
            } catch (Throwable error) {
                OPENING.set(false);
                NativeMusicDiagnostics.showCaught(context, "Caught launch failure", error);
                NativeMusicDiagnostics.completed();
                Log.e(TAG, "Native Music could not open at: " + phase, error);
            }
        });
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        open(this);
        finish();
    }

    private static void showToast(Context context, String message) {
        MAIN.post(() -> Toast.makeText(context, message, Toast.LENGTH_LONG).show());
    }

    private static final class ExtractedPaths {
        final File dir;
        final File apk;
        final File lib;
        ExtractedPaths(File dir, File apk, File lib) {
            this.dir = dir;
            this.apk = apk;
            this.lib = lib;
        }
    }

    private static ExtractedPaths extractIfNeeded(Context context) throws IOException {
        synchronized (LOCK) {
            phase = "extracting plugin APK";
            NativeMusicDiagnostics.phase(phase);
            File dir = new File(context.getCodeCacheDir(), "ultra_native_music_consistent_v6");
            if (!dir.exists() && !dir.mkdirs()) throw new IOException("Cannot create plugin dir: " + dir);
            File apk = new File(dir, "music.apk");
            File lib = new File(dir, "lib");
            String stamp = buildHostStamp(context);
            File marker = new File(dir, ".ready_" + stamp);

            if (!marker.exists() || !apk.isFile() || !lib.isDirectory()) {
                cleanOldMarkers(dir);
                File tmpApk = new File(dir, "music.apk.tmp");
                try (InputStream in = context.getAssets().open(ASSET_APK);
                     OutputStream out = new FileOutputStream(tmpApk)) {
                    byte[] buffer = new byte[65536];
                    int count;
                    while ((count = in.read(buffer)) != -1) out.write(buffer, 0, count);
                    out.flush();
                }
                if (apk.exists() && !apk.delete()) throw new IOException("Cannot replace stale plugin APK");
                if (!tmpApk.renameTo(apk)) throw new IOException("Cannot finalize plugin APK");
                apk.setReadOnly();

                phase = "native library extraction";
                NativeMusicDiagnostics.phase(phase);
                if (lib.exists() && !deleteRecursively(lib)) {
                    throw new IOException("Cannot clear stale native library directory");
                }
                if (!lib.mkdirs() && !lib.isDirectory()) throw new IOException("Cannot create lib dir: " + lib);
                extractNativeLibs(apk, lib);
                if (!marker.createNewFile() && !marker.exists()) {
                    throw new IOException("Cannot write extraction marker");
                }
            } else {
                apk.setReadOnly();
            }
            return new ExtractedPaths(dir, apk, lib);
        }
    }

    private static boolean deleteRecursively(File file) {
        if (file == null || !file.exists()) return true;
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) if (!deleteRecursively(child)) return false;
        }
        return file.delete();
    }

    private static MusicPluginLoader ensurePluginLoaded(Context context, ExtractedPaths paths) throws Exception {
        synchronized (LOCK) {
            if (loader != null) return loader;
            phase = "loader initialization";
            NativeMusicDiagnostics.phase(phase);
            if (!loggerReady) {
                LoggerFactory.setILoggerFactory(name -> (Logger) Proxy.newProxyInstance(
                    Logger.class.getClassLoader(), new Class[]{Logger.class},
                    (proxy, method, args) -> method.getReturnType() == boolean.class ? false : null));
                loggerReady = true;
            }
            MusicPluginLoader next = new MusicPluginLoader(context);
            next.onCreate();
            DelegateProviderHolder.setDelegateProvider(MusicPluginLoader.KEY, next);
            ContentProviderDelegateProviderHolder.setContentProviderDelegateProvider(next);

            byte[] params;
            Parcel parcel = Parcel.obtain();
            try {
                new LoadParameters("ultra_native_music", "music", null,
                    new String[]{"paradox.music.nativeui"}).writeToParcel(parcel, 0);
                params = parcel.marshall();
            } finally {
                parcel.recycle();
            }

            phase = "classloader, resources and application construction";
            NativeMusicDiagnostics.phase(phase);
            next.loadPlugin(new InstalledApk(paths.apk.getAbsolutePath(), paths.dir.getAbsolutePath(),
                paths.lib.getAbsolutePath(), params)).get(60, TimeUnit.SECONDS);

            phase = "Music application metadata";
            NativeMusicDiagnostics.phase(phase);
            PackageInfo archive = context.getPackageManager().getPackageArchiveInfo(
                paths.apk.getAbsolutePath(), PackageManager.GET_META_DATA);
            if (archive == null || archive.applicationInfo == null) {
                throw new IllegalStateException("Cannot read embedded Music application metadata");
            }
            next.getPluginParts("music").getApplication().getApplicationInfo().metaData =
                archive.applicationInfo.metaData;
            loader = next;
            return next;
        }
    }

    private static void ensureApplicationCreated(MusicPluginLoader target) throws Exception {
        // Do not hold LOCK while Android's main thread runs plugin Application.onCreate().
        // Plugin startup can call host/plugin callbacks which may need loader state.
        synchronized (LOCK) {
            if (applicationCreated) return;
        }

        phase = "Music application attach, providers and onCreate";
        NativeMusicDiagnostics.phase(phase);
        final Throwable[] startupFailure = new Throwable[1];
        CountDownLatch done = new CountDownLatch(1);
        MAIN.post(() -> {
            Thread.UncaughtExceptionHandler previous = Thread.getDefaultUncaughtExceptionHandler();
            try {
                NativeMusicStartupTrace.event("Application main-thread lifecycle begin");
                target.callApplicationOnCreate("music");
                NativeMusicStartupTrace.event("Application main-thread lifecycle end");
                synchronized (LOCK) {
                    applicationCreated = true;
                }
            } catch (Throwable error) {
                startupFailure[0] = error;
            } finally {
                if (Thread.getDefaultUncaughtExceptionHandler() != previous) {
                    Thread.setDefaultUncaughtExceptionHandler(previous);
                }
                done.countDown();
            }
        });

        if (!done.await(60, TimeUnit.SECONDS)) {
            throw new IllegalStateException(
                "Music application startup timed out after 60 seconds; inspect startup diagnostics");
        }
        if (startupFailure[0] != null) {
            throw new Exception("Music application startup failed", startupFailure[0]);
        }
    }

    private static void extractNativeLibs(File apk, File libDir) throws IOException {
        try (ZipFile zip = new ZipFile(apk)) {
            String prefix = selectAbiPrefix(zip);
            if (prefix == null) return;
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().startsWith(prefix) && entry.getName().endsWith(".so")) {
                    File output = new File(libDir, new File(entry.getName()).getName());
                    File temporary = new File(libDir, output.getName() + ".tmp");
                    try (InputStream in = zip.getInputStream(entry);
                         OutputStream out = new FileOutputStream(temporary)) {
                        byte[] buffer = new byte[65536];
                        int count;
                        while ((count = in.read(buffer)) != -1) out.write(buffer, 0, count);
                        out.flush();
                    }
                    if (output.exists() && !output.delete()) throw new IOException("Cannot replace " + output.getName());
                    if (!temporary.renameTo(output)) throw new IOException("Cannot finalize " + output.getName());
                    output.setReadable(true, true);
                    output.setExecutable(true, true);
                }
            }
        }
    }

    private static String selectAbiPrefix(ZipFile zip) {
        for (String abi : Build.SUPPORTED_ABIS) {
            String candidate = "lib/" + abi + "/";
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(candidate) && name.endsWith(".so")) return candidate;
            }
        }
        return null;
    }

    private static String buildHostStamp(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            long version = Build.VERSION.SDK_INT >= 28 ? info.getLongVersionCode() : info.versionCode;
            return version + "_" + info.lastUpdateTime;
        } catch (Throwable ignored) {
            return "v1";
        }
    }

    private static void cleanOldMarkers(File dir) {
        File[] files = dir.listFiles((directory, name) -> name.startsWith(".ready_"));
        if (files != null) for (File file : files) file.delete();
    }
}
