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
            if (!superCreateCalled) {
                superOnCreate(null);
            }
            Toast.makeText(getApplicationContext(), "Music could not start", Toast.LENGTH_LONG).show();
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
        NativeMusicDiagnostics.completed();
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
        Log.e(TAG, phase, e);
        StringWriter s = new StringWriter();
        e.printStackTrace(new PrintWriter(s));
        TextView t = new TextView(a);
        t.setTextSize(12);
        t.setPadding(24, 48, 24, 24);
        t.setTextIsSelectable(true);
        t.setText("Music failed at: " + phase + "\n\n" + s);
        ScrollView scroll = new ScrollView(a);
        scroll.addView(t);
        a.setContentView(scroll);
    }
}
