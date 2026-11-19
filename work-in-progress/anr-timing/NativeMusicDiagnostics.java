package paradox.music.nativeui;
import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import java.util.*;

/** Local-only diagnostic. Does not suppress or change the normal crash handler. */
public final class NativeMusicDiagnostics {
 private static Application app;
 private static File state, report;
 private static volatile String phase="not started";
 private static boolean registered, shown;
 private static volatile boolean started;
 private static String previous;
 private static Thread.UncaughtExceptionHandler wrapper;
 private static java.lang.ref.WeakReference<Activity> resumed = new java.lang.ref.WeakReference<Activity>(null);
 public static synchronized void install(Context context){
  try {
   if(app==null){app=(Application)context.getApplicationContext();NativeMusicStartupTrace.install(app);state=new File(app.getFilesDir(),"ultra-music-startup-state.txt");report=new File(app.getFilesDir(),"ultra-music-crash.txt");previous=read(report);if(previous==null && state.exists()) previous="Music stopped before startup finished. No Java exception was captured. A native crash or explicit process exit cannot be caught by Java.\n\n"+read(state)+exitInfo()+"\n"+NativeMusicStartupTrace.readSaved();}
   Thread.UncaughtExceptionHandler current=Thread.getDefaultUncaughtExceptionHandler();
   if(current!=wrapper){final Thread.UncaughtExceptionHandler prior=current;wrapper=(thread,error)->{if(started)capture("Uncaught exception on thread "+thread.getName(),error);if(prior!=null)prior.uncaughtException(thread,error);else{android.os.Process.killProcess(android.os.Process.myPid());System.exit(10);}};Thread.setDefaultUncaughtExceptionHandler(wrapper);}
   if(!registered){registered=true;app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks(){public void onActivityResumed(Activity a){resumed=new java.lang.ref.WeakReference<Activity>(a);show(a);}public void onActivityCreated(Activity a,Bundle b){}public void onActivityStarted(Activity a){}public void onActivityPaused(Activity a){if(resumed.get()==a)resumed.clear();}public void onActivityStopped(Activity a){}public void onActivitySaveInstanceState(Activity a,Bundle b){}public void onActivityDestroyed(Activity a){}});}
  }catch(Throwable ignored){}
 }
 public static void begin(){NativeMusicStartupTrace.begin();started=true;phase("Music button tapped");}
 public static void phase(String value){NativeMusicStartupTrace.event(value);phase=value;if(!started)return;try{if(state!=null)write(state,"Time: "+new Date()+"\nStartup phase: "+phase+"\n");}catch(Throwable ignored){}}
 public static void capture(String where,Throwable error){try{if(report==null)return;StringWriter s=new StringWriter();if(error!=null)error.printStackTrace(new PrintWriter(s));write(report,"YouTube Ultra Music diagnostic\nTime: "+new Date()+"\nStartup phase: "+phase+"\n"+where+"\n\n"+s+"\n"+NativeMusicStartupTrace.readSaved());}catch(Throwable ignored){}}
 public static void showCaught(Context context,String where,Throwable error){
  capture(where,error);
  new Handler(Looper.getMainLooper()).post(()->{
   previous=read(report);shown=false;
   Activity a=context instanceof Activity?(Activity)context:null;
   if(a==null||a.isFinishing()||(Build.VERSION.SDK_INT>=17&&a.isDestroyed()))a=resumed.get();
   if(a!=null&&!a.isFinishing()&&(Build.VERSION.SDK_INT<17||!a.isDestroyed()))show(a);
  });
 }
 public static void exitAttempt(int code){capture("Explicit System.exit("+code+")",new Throwable("Process exit call site"));}
 public static void completed(){NativeMusicStartupTrace.end();try{if(state!=null)state.delete();}catch(Throwable ignored){}}
 private static String read(File f){try{if(f==null||!f.exists())return null;ByteArrayOutputStream out=new ByteArrayOutputStream();try(InputStream in=new FileInputStream(f)){byte[] b=new byte[4096];int n;while((n=in.read(b))!=-1&&out.size()<65536)out.write(b,0,n);}return out.toString("UTF-8");}catch(Throwable e){return null;}}
 private static void write(File f,String s)throws Exception{try(FileOutputStream out=new FileOutputStream(f)){out.write(s.getBytes("UTF-8"));out.getFD().sync();}}
 private static String exitInfo(){if(Build.VERSION.SDK_INT<30)return "";try{ActivityManager am=(ActivityManager)app.getSystemService(Context.ACTIVITY_SERVICE);List<ApplicationExitInfo> list=am.getHistoricalProcessExitReasons(app.getPackageName(),0,2);StringBuilder s=new StringBuilder("\nAndroid recent process exits:\n");for(ApplicationExitInfo x:list)s.append("reason=").append(x.getReason()).append(" status=").append(x.getStatus()).append(" time=").append(new Date(x.getTimestamp())).append(" description=").append(x.getDescription()).append('\n');return s.toString();}catch(Throwable e){return "\nAndroid exit reason unavailable: "+e;}}
 private static void show(Activity a){if(shown||previous==null||a.isFinishing())return;shown=true;final String text=previous;try{TextView view=new TextView(a);view.setText(text);view.setTextSize(12);view.setPadding(24,24,24,24);view.setTextIsSelectable(true);ScrollView scroll=new ScrollView(a);scroll.addView(view);new AlertDialog.Builder(a).setTitle("Music startup diagnostic").setView(scroll).setPositiveButton("Copy",(d,w)->{android.content.ClipboardManager cm=(android.content.ClipboardManager)a.getSystemService(Context.CLIPBOARD_SERVICE);cm.setPrimaryClip(ClipData.newPlainText("Music diagnostic",text));}).setNegativeButton("Close",null).show();if(report!=null)report.delete();if(state!=null)state.delete();}catch(Throwable ignored){shown=false;}}
}
