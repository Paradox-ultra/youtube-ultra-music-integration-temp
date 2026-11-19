package paradox.music.nativeui;
import java.util.*;
/** Only known plugin connections can enter the main-thread plugin dispatcher. */
public final class NativeMusicServiceConnections {
 private static final Set<Object> PLUGIN=Collections.newSetFromMap(new IdentityHashMap<Object,Boolean>());
 public static synchronized void pluginBinding(Object connection){PLUGIN.add(connection);}
 public static synchronized boolean isPlugin(Object connection){return PLUGIN.contains(connection);}
 public static synchronized void pluginUnbound(Object connection){PLUGIN.remove(connection);}
 // Compatibility with the previously patched framework bind sites. No external tracking is needed.
 public static void bound(Object context,Object connection){}
 public static boolean takeExternal(Object context,Object connection){return !isPlugin(connection);}
}
