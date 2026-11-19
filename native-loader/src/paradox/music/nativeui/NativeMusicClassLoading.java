package paradox.music.nativeui;
import java.util.concurrent.atomic.AtomicLong;
/** Counts only the plugin's default-package fast path. Never changes packaged-class delegation. */
public final class NativeMusicClassLoading {
 private static final AtomicLong hits=new AtomicLong(),misses=new AtomicLong(),nanos=new AtomicLong();
 public static long start(){return System.nanoTime();}
 public static void done(long start,boolean hit){nanos.addAndGet(System.nanoTime()-start);if(hit)hits.incrementAndGet();else misses.incrementAndGet();}
 public static String summary(){return "Plugin default-package lookups: found="+hits.get()+", absent="+misses.get()+", inclusive lookup time="+(nanos.get()/1000000)+"ms (nested times overlap)";}
}
