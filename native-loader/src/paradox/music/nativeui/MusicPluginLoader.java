package paradox.music.nativeui;

import android.content.ComponentName;
import android.content.Context;
import com.tencent.shadow.core.loader.ShadowPluginLoader;
import com.tencent.shadow.core.loader.infos.ContainerProviderInfo;
import com.tencent.shadow.core.loader.managers.ComponentManager;
import java.util.Collections;
import java.util.List;

public final class MusicPluginLoader extends ShadowPluginLoader {
    public static final String KEY = "ULTRA_NATIVE_MUSIC";
    private final MusicComponents components;
    public MusicPluginLoader(Context context) {
        super(context);
        components = new MusicComponents(context);
    }
    @Override public String getDelegateProviderKey() { return KEY; }
    @Override public ComponentManager getComponentManager() { return components; }

    private static final class MusicComponents extends ComponentManager {
        private final Context context;
        MusicComponents(Context context) { this.context = context; }
        @Override public ComponentName onBindContainerActivity(ComponentName plugin) {
            String name = plugin.getClassName().equals("com.google.android.apps.youtube.music.activities.MusicActivity")
                ? MusicTaskActivity.class.getName() : MusicProxyActivity.class.getName();
            return new ComponentName(context.getPackageName(), name);
        }
        @Override public ContainerProviderInfo onBindContainerContentProvider(ComponentName plugin) {
            return new ContainerProviderInfo("com.tencent.shadow.core.runtime.container.PluginContainerContentProvider",
                context.getPackageName() + ".native_music.provider");
        }
        @Override public List<ComponentName> getContainerServicePool() {
            return Collections.singletonList(new ComponentName(context.getPackageName(), MusicProxyService.class.getName()));
        }
    }
}
