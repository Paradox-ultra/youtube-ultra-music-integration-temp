package paradox.music.nativeui;
import android.os.*;import java.lang.reflect.*;import java.util.concurrent.*;
/** Preserves main-thread lifecycle execution and returns failures to the requesting worker. */
public final class NativeMusicMainThread {
 private static final Handler MAIN=new Handler(Looper.getMainLooper());
 public static Object invoke(Object target,String method,Class[] types,Object[] args){return call(()->{try{Method m=target.getClass().getDeclaredMethod(method,types);m.setAccessible(true);return m.invoke(target,args);}catch(InvocationTargetException e){throw e.getCause();}});}
 private interface Work{Object run()throws Throwable;}
 private static Object call(Work work){if(Looper.myLooper()==Looper.getMainLooper())return perform(work);FutureTask<Object> task=new FutureTask<>(()->perform(work));if(!MAIN.post(task))throw new IllegalStateException("Main looper rejected Music work");try{return task.get(60,TimeUnit.SECONDS);}catch(InterruptedException e){task.cancel(false);Thread.currentThread().interrupt();throw new IllegalStateException("Interrupted waiting for Music lifecycle",e);}catch(TimeoutException e){task.cancel(false);throw new IllegalStateException("Music main-thread lifecycle timed out",e);}catch(ExecutionException e){Throwable t=e.getCause();if(t instanceof RuntimeException)throw (RuntimeException)t;if(t instanceof Error)throw (Error)t;throw new IllegalStateException(t);}}
 private static Object perform(Work work){try{return work.run();}catch(RuntimeException|Error e){throw e;}catch(Throwable e){throw new IllegalStateException("Music lifecycle failed",e);}}
 public static void initializeWebView(android.content.Context context){call(()->{android.webkit.WebView web=null;try{web=new android.webkit.WebView(context);}catch(Exception unavailable){android.util.Log.w("UltraMusic","WebView initialization unavailable",unavailable);}finally{if(web!=null)web.destroy();}return null;});}
}
