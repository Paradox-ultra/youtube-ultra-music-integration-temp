package paradox.music.nativeui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.tencent.shadow.core.runtime.container.PluginContainerActivity;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MusicProxyActivity extends PluginContainerActivity {
    private static final String TAG = "UltraMusic";
    private boolean startupFailed;
    private boolean readyProbeStarted;
    private boolean superCreateCalled;

    @Override
    protected String getDelegateProviderKey() {
        return MusicPluginLoader.KEY;
    }

    @Override
    public void superOnCreate(Bundle savedInstanceState) {
        superCreateCalled = true;
        super.superOnCreate(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle b) {
        NativeMusicDiagnostics.install(getApplicationContext());
        if (!NativeMusicActivity.isApplicationReady()) {
            startupFailed = true;
            ultraDetachFailedActivity();
            NativeMusicDiagnostics.recoveryNote("Cold Music activity restored before its loader was ready; returning to Ultra");
            superOnCreate(null);
            Intent home = getPackageManager().getLaunchIntentForPackage(getPackageName());
            if (home != null) {
                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
            }
            finish();
            return;
        }
        try {
            NativeMusicDiagnostics.phase("Music activity onCreate");
            super.onCreate(b);
        } catch (Throwable e) {
            startupFailed = true;
            NativeMusicDiagnostics.capture("Caught Music activity onCreate failure",e);
            Log.e(TAG, "Music activity onCreate failed", e);
            // The original delegate failure must not be hidden by fallback callbacks.
            ultraDetachFailedActivity();
            try {
                if (!superCreateCalled) superOnCreate(null);
            } catch (Throwable recovery) {
                if (recovery != e) e.addSuppressed(recovery);
            }
            NativeMusicDiagnostics.showCaught(getApplicationContext(), "Music activity initialization failed; recovery errors are suppressed below",e);
            try { Toast.makeText(getApplicationContext(), "Music could not start; report saved", Toast.LENGTH_LONG).show(); }
            catch (Throwable ignored) {}
            finish();
        }
    }

    @Override
    public void onStart() {
        if (startupFailed) {
            superOnStart();
            return;
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        if (startupFailed) {
            superOnResume();
            return;
        }
        super.onResume();
        NativeMusicDiagnostics.phase("Music Activity resumed; waiting for drawn selected Library tab");
        if(!readyProbeStarted){readyProbeStarted=true;NativeMusicReadyProbe.watch(this);}
    }

    @Override
    public void onPause() {
        if (startupFailed) {
            superOnPause();
            return;
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (startupFailed) {
            superOnStop();
            return;
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (startupFailed) {
            superOnDestroy();
            return;
        }
        super.onDestroy();
    }

    public static void showFailure(Activity a, String phase, Throwable e) {
        NativeMusicDiagnostics.showCaught(a,phase,e);
    }
}
