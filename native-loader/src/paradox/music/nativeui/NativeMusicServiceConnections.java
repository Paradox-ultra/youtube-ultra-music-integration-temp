package paradox.music.nativeui;
import java.util.*;
/** Identity-based ownership routing. Holds no service lifecycle work and never waits for main. */
public final class NativeMusicServiceConnections {
 private static final Map<Object,Set<Object>> EXTERNAL=new WeakHashMap<>();
 public static synchronized void bound(Object context,Object connection){Set<Object> set=EXTERNAL.get(context);if(set==null){set=Collections.newSetFromMap(new IdentityHashMap<Object,Boolean>());EXTERNAL.put(context,set);}set.add(connection);}
 public static synchronized boolean takeExternal(Object context,Object connection){Set<Object> set=EXTERNAL.get(context);if(set==null||!set.remove(connection))return false;if(set.isEmpty())EXTERNAL.remove(context);return true;}
}
