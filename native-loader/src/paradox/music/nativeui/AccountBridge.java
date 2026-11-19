package paradox.music.nativeui;

import android.content.SharedPreferences;
import android.os.Looper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/** Shares only the host's selected native identity. Never chooses the first device account. */
public final class AccountBridge {
    private static volatile SharedPreferences host;
    private static volatile SharedPreferences plugin;
    private static volatile Object controller;
    private static volatile boolean identitySeeded;

    private static final String[] KEYS = {
        "user_account",
        "user_identity",
        "persona_account",
        "IS_INCOGNITO_SESSION_IDENTITY",
        "user_identity_id",
        "identity_version",
        "datasync_id",
        "IS_UNICORN_CHILD_ACCOUNT",
        "HAS_GRIFFIN_POLICY",
        "IS_CHILD_ACCOUNT_OVER_13",
        "delegation_type",
        "delegation_context"
    };

    public static void bindHost(SharedPreferences value) {
        host = value;
    }

    /** Called via insertBeforeBody($1) before donor constructors read SharedPreferences. */
    public static synchronized void seedPluginPreferences(SharedPreferences prefs) {
        requireSelectedHost();
        if (prefs == null) {
            throw new IllegalStateException("Music identity preferences are null");
        }
        plugin = prefs;
        copyIdentity();
        identitySeeded = true;
    }

    /** Called via insertAfter(this, $1) after donor constructor completes. */
    public static void bindPlugin(Object value, SharedPreferences prefs) {
        controller = value;
        if (prefs != null) {
            plugin = prefs;
        }
    }

    public static boolean matchesSelectedAccount(Object value) {
        if (!(value instanceof android.accounts.Account)) return false;
        SharedPreferences h = host;
        return h != null && ((android.accounts.Account) value).name.equals(h.getString("user_account", null));
    }

    public static String diagnosticSummary() {
        SharedPreferences h = host, p = plugin;
        String ha = h == null ? null : h.getString("user_account", null);
        String hi = h == null ? null : h.getString("user_identity_id", null);
        String pa = p == null ? null : p.getString("user_account", null);
        String pi = p == null ? null : p.getString("user_identity_id", null);
        return "selected-host-present=" + (ha != null && hi != null)
            + " plugin-preferences-bound=" + (p != null)
            + " identity-seeded=" + identitySeeded
            + " controller-bound=" + (controller != null)
            + " account-match=" + (ha != null && ha.equals(pa))
            + " identity-match=" + (hi != null && hi.equals(pi))
            + " incognito=" + (h != null && h.getBoolean("IS_INCOGNITO_SESSION_IDENTITY", false));
    }

    public static boolean hasSelectedHost() {
        SharedPreferences h = host;
        return h != null
            && h.getString("user_account", null) != null
            && h.getString("user_identity_id", null) != null;
    }

    public static void requireSelectedHost() {
        if (!hasSelectedHost()) {
            throw new IllegalStateException("Choose your account in YouTube first, then open Music");
        }
    }

    public static synchronized void copyIdentity() {
        if (host == null || plugin == null) {
            throw new IllegalStateException("Identity preferences are not bound");
        }
        Map<String, ?> values = host.getAll();
        SharedPreferences.Editor e = plugin.edit();
        for (String k : KEYS) {
            Object v = values.get(k);
            if (v == null) {
                e.remove(k);
            } else if (v instanceof String) {
                e.putString(k, (String) v);
            } else if (v instanceof Boolean) {
                e.putBoolean(k, (Boolean) v);
            } else if (v instanceof Integer) {
                e.putInt(k, (Integer) v);
            } else if (v instanceof Long) {
                e.putLong(k, (Long) v);
            }
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            e.apply();
        } else if (!e.commit()) {
            throw new IllegalStateException("Could not synchronize Music identity");
        }
    }

    public static void synchronizeIfControllerBound() throws Exception {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Identity synchronization must not run on main thread");
        }
        requireSelectedHost();
        if (controller == null) {
            if (plugin != null) {
                copyIdentity();
                identitySeeded = true;
            }
            return;
        }
        synchronizeAndVerify();
    }

    public static void synchronizeAndVerify() throws Exception {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("synchronizeAndVerify must not run on main thread");
        }
        requireSelectedHost();
        if (!identitySeeded || plugin == null) {
            throw new IllegalStateException("Music identity was not seeded from YouTube");
        }
        if (controller == null) {
            throw new IllegalStateException("Music identity controller is not ready");
        }
        copyIdentity();
        String account = host.getString("user_account", null);
        String id = host.getString("user_identity_id", null);
        if (account == null || id == null) {
            if ((Boolean) controller.getClass().getMethod("r").invoke(controller)) {
                throw new IllegalStateException("Music still has an identity while YouTube is signed out");
            }
            return;
        }
        Class<?> identity = controller.getClass().getClassLoader().loadClass("adwv");
        Class<?> implementation = controller.getClass().getClassLoader().loadClass("adxp");
        Constructor<?> factory = implementation.getDeclaredConstructor(
            String.class, String.class, String.class,
            boolean.class, boolean.class, boolean.class,
            String.class, boolean.class, boolean.class,
            boolean.class, int.class, String.class
        );
        factory.setAccessible(true);
        Object selected = factory.newInstance(
            id,
            account,
            host.getString("user_identity", ""),
            host.getBoolean("IS_INCOGNITO_SESSION_IDENTITY", false),
            false,
            host.getBoolean("persona_account", false),
            host.getString("datasync_id", id),
            host.getBoolean("IS_UNICORN_CHILD_ACCOUNT", false),
            host.getBoolean("HAS_GRIFFIN_POLICY", false),
            host.getBoolean("IS_CHILD_ACCOUNT_OVER_13", false),
            host.getInt("delegation_type", 1) + 1,
            host.getString("delegation_context", "NO_DELEGATION_CONTEXT")
        );
        Object result = controller.getClass().getMethod("g", identity).invoke(controller, selected);
        if (result instanceof Future) {
            ((Future<?>) result).get(30, TimeUnit.SECONDS);
        }
        Object current = controller.getClass().getMethod("d").invoke(controller);
        Method identityId = identity.getMethod("d");
        identityId.setAccessible(true);
        if (current == null || !id.equals(identityId.invoke(current))) {
            throw new IllegalStateException("Music identity did not match YouTube's selected identity");
        }
    }
}
