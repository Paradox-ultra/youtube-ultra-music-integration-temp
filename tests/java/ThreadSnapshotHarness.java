package paradox.music.nativeui;
import java.lang.reflect.*;import java.util.*;import java.util.concurrent.*;
public class ThreadSnapshotHarness {
 public static void main(String[] args)throws Exception {
  android.os.Looper.getMainLooper();CountDownLatch latch=new CountDownLatch(1);BlockingQueue<Object> idle=new LinkedBlockingQueue<>();
  Thread google=new Thread(()->{try{latch.await();}catch(Exception ignored){}},"GoogleApiHandler-fixture");Thread other=new Thread(()->{try{latch.await();}catch(Exception ignored){}},"fixture-latch");Thread queue=new Thread(()->{try{idle.take();}catch(Exception ignored){}},"fixture-idle-queue");google.start();other.start();queue.start();Thread.sleep(50);
  String result=NativeMusicStartupTrace.waitingThreads(3500);if(!result.contains("GoogleApiHandler-fixture")||!result.contains("fixture-latch")||result.contains("Thread fixture-idle-queue")||!result.contains("CountDownLatch.await"))throw new AssertionError(result);if(result.indexOf("GoogleApiHandler-fixture")>result.indexOf("fixture-latch"))throw new AssertionError("Google thread not first");
  latch.countDown();idle.add(new Object());google.join();other.join();queue.join();System.out.println("PASS contending-thread selection: GoogleApiHandler first, latch waiter included, idle queue excluded");System.exit(0);
 }
}
