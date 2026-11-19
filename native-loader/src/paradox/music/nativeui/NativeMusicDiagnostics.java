package paradox.music.nativeui;
import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import java.util.*;

/** Local-only diagnostic. Does not suppress or change the normal crash handler. */
public final class NativeMusicDiagnostics {
 private static final java.util.concurrent.ExecutorService STATE_IO=java.util.concurrent.Executors.newSingleThreadExecutor(r->{Thread t=new Thread(r,"Ultra-Music-Diagnostic-IO");t.setDaemon(true);return t;});
 private static Application app;
 private static File state, report, lastAttempt;
 private static volatile String attempt="none";
 private static volatile long attemptStart;
 private static volatile long libraryMs=-1;
 private static volatile String phase="not started";
 private static boolean registered, shown;
 private static volatile boolean started;
 private static volatile String previous;
 private static File export;
 private static volatile boolean exportReady;
 private static Thread.UncaughtExceptionHandler wrapper;
 private static java.lang.ref.WeakReference<Activity> resumed = new java.lang.ref.WeakReference<Activity>(null);
 public static synchronized void install(Context context){
  try {
   if(app==null){app=(Application)context.getApplicationContext();NativeMusicStartupTrace.install(app);state=new File(app.getFilesDir(),"ultra-music-startup-state.txt");lastAttempt=new File(app.getFilesDir(),"ultra-music-last-attempt.txt");report=new File(app.getFilesDir(),"ultra-music-crash.txt");STATE_IO.execute(()->{previous=read(report);if(previous==null && state.exists())previous="Music stopped before startup finished. No Java exception was captured. A native crash or explicit process exit cannot be caught by Java.\n\n"+read(state)+"\n"+NativeMusicStartupTrace.readSummary()+exitInfo();if(previous!=null){String latest=read(lastAttempt);previous="PRIOR SESSION REPORT. Not proof of a new crash.\nLatest recorded attempt:\n"+(latest==null?"unavailable":latest)+"\n"+NativeMusicStartupTrace.readSummary()+"\n\nRetained report:\n"+previous;}prepareExport();new Handler(Looper.getMainLooper()).post(()->{Activity a=resumed.get();if(a!=null)show(a);});});}
   Thread.UncaughtExceptionHandler current=Thread.getDefaultUncaughtExceptionHandler();
   if(current!=wrapper){final Thread.UncaughtExceptionHandler prior=current;wrapper=(thread,error)->{capture("Uncaught exception on thread "+thread.getName(),error);if(prior!=null)prior.uncaughtException(thread,error);else{android.os.Process.killProcess(android.os.Process.myPid());System.exit(10);}};Thread.setDefaultUncaughtExceptionHandler(wrapper);}
   if(!registered){registered=true;app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks(){public void onActivityResumed(Activity a){resumed=new java.lang.ref.WeakReference<Activity>(a);show(a);}public void onActivityCreated(Activity a,Bundle b){}public void onActivityStarted(Activity a){}public void onActivityPaused(Activity a){if(resumed.get()==a)resumed.clear();}public void onActivityStopped(Activity a){}public void onActivitySaveInstanceState(Activity a,Bundle b){}public void onActivityDestroyed(Activity a){}});}
  }catch(Throwable ignored){}
 }
 public static void begin(){shown=false;previous=null;attempt=Long.toString(System.currentTimeMillis());attemptStart=SystemClock.elapsedRealtime();libraryMs=-1;NativeMusicStartupTrace.begin();started=true;phase("Music button tapped");}
 public static void phase(String value){NativeMusicStartupTrace.event(value);phase=value;if(!started)return;persistAlive();}
 public static String attemptSummary(){return "Build: music-fastpath-20261119\nAttempt: "+attempt+"\nTime: "+new Date()+"\nElapsed: "+(attemptStart==0?0:SystemClock.elapsedRealtime()-attemptStart)+"ms\nStartup phase: "+phase+"\nStart-to-Library-ready: "+(libraryMs<0?"NOT CONFIRMED":libraryMs+"ms")+"\n"+NativeMusicClassLoading.summary()+"\n";}
 public static void heartbeat(){if(started)persistAlive();}
 private static void persistAlive(){final String snapshot=attemptSummary();STATE_IO.execute(()->{try{if(lastAttempt!=null)write(lastAttempt,snapshot);if(state!=null)write(state,snapshot);}catch(Throwable ignored){}});}
 public static void libraryReady(){libraryMs=SystemClock.elapsedRealtime()-attemptStart;phase("Library-ready: selected Library tab drawn with window focus");completed();}
 public static void capture(String where,Throwable error){try{if(report==null)return;StringWriter s=new StringWriter();if(error!=null)error.printStackTrace(new PrintWriter(s));write(report,"YouTube Ultra Music diagnostic\n"+attemptSummary()+"\n"+where+"\n\n"+s);}catch(Throwable ignored){}}
 public static void showCaught(Context context,String where,Throwable error){
  capture(where,error);
  STATE_IO.execute(()->{previous=read(report);prepareExport();new Handler(Looper.getMainLooper()).post(()->{Activity a=context instanceof Activity?(Activity)context:null;if(a==null||a.isFinishing()||(Build.VERSION.SDK_INT>=17&&a.isDestroyed()))a=resumed.get();if(a!=null&&!a.isFinishing()&&(Build.VERSION.SDK_INT<17||!a.isDestroyed()))show(a);});});
 }
 public static void recoveryNote(String text){if(report!=null&&!report.exists())capture(text,new IllegalStateException(text));}
 public static void exitAttempt(int code){capture("Explicit System.exit("+code+")",new Throwable("Process exit call site"));}
 public static void completed(){started=false;NativeMusicStartupTrace.end();STATE_IO.execute(()->{try{if(state!=null)state.delete();}catch(Throwable ignored){}});}
 private static String read(File f){try{if(f==null||!f.exists())return null;ByteArrayOutputStream out=new ByteArrayOutputStream();try(InputStream in=new FileInputStream(f)){byte[] b=new byte[4096];int n;while((n=in.read(b))!=-1&&out.size()<65536)out.write(b,0,n);}return out.toString("UTF-8");}catch(Throwable e){return null;}}
 private static void write(File f,String s)throws Exception{try(FileOutputStream out=new FileOutputStream(f)){out.write(s.getBytes("UTF-8"));out.getFD().sync();}}
 private static String exitInfo(){if(Build.VERSION.SDK_INT<30)return "";try{ActivityManager am=(ActivityManager)app.getSystemService(Context.ACTIVITY_SERVICE);List<ApplicationExitInfo> list=am.getHistoricalProcessExitReasons(app.getPackageName(),0,4);StringBuilder s=new StringBuilder("\nAndroid recent process exits:\n");for(ApplicationExitInfo x:list){s.append("reason=").append(x.getReason()).append(" status=").append(x.getStatus()).append(" time=").append(new Date(x.getTimestamp())).append(" description=").append(x.getDescription()).append('\n');if(x.getReason()==ApplicationExitInfo.REASON_ANR){try(InputStream in=x.getTraceInputStream()){if(in!=null){ByteArrayOutputStream out=new ByteArrayOutputStream();byte[] buf=new byte[4096];int n;while(out.size()<192*1024&&(n=in.read(buf,0,Math.min(buf.length,192*1024-out.size())))>0)out.write(buf,0,n);s.append("Android ANR trace:\n").append(out.toString("UTF-8")).append('\n');}}catch(Throwable unavailable){s.append("ANR trace unavailable: ").append(unavailable.getClass().getName()).append('\n');}}}return s.toString();}catch(Throwable e){return "\nAndroid exit reason unavailable: "+e;}}
 private static void prepareExport(){try{if(previous==null)return;File dir=new File(app.getCacheDir(),"photos/ultra-music-reports");if(!dir.isDirectory()&&!dir.mkdirs())return;export=new File(dir,"ultra-music-diagnostic.txt");String full=previous+"\n"+NativeMusicStartupTrace.readSaved();write(export,full);exportReady=true;}catch(Throwable ignored){exportReady=false;}}
 private static String displaySummary(String value){if(value==null)return "";int cap=Math.min(value.length(),12*1024);String text=value.substring(0,cap);String[] lines=text.split("\n",101);if(lines.length>100){StringBuilder shortText=new StringBuilder();for(int i=0;i<100;i++)shortText.append(lines[i]).append('\n');text=shortText.toString();}return text+"\n\nFull report is saved locally. Copy copies this summary. Export lets you save the complete report.";}
 private static void acknowledge(){STATE_IO.execute(()->{if(report!=null)report.delete();if(!started&&"none".equals(attempt)&&state!=null)state.delete();});}
 private static java.lang.reflect.Method[] fileProviderMethods(Class<?> type){java.util.List<java.lang.reflect.Method> result=new java.util.ArrayList<>();for(Class<?> c=type;c!=null;c=c.getSuperclass())java.util.Collections.addAll(result,c.getDeclaredMethods());return result.toArray(new java.lang.reflect.Method[0]);}
 private static void exportReport(Activity a){if(!exportReady||export==null){Toast.makeText(a,"Report file is not ready",Toast.LENGTH_SHORT).show();return;}try{Class<?> fp=Class.forName("com.google.android.apps.youtube.app.common.util.MainAppFileProviderCompat").getSuperclass();android.net.Uri uri=null;for(java.lang.reflect.Method m:fileProviderMethods(fp)){if(java.lang.reflect.Modifier.isStatic(m.getModifiers())&&m.getReturnType()==android.net.Uri.class&&java.util.Arrays.equals(m.getParameterTypes(),new Class[]{Context.class,String.class,File.class})){m.setAccessible(true);uri=(android.net.Uri)m.invoke(null,app,app.getPackageName()+".fileprovider",export);break;}}if(uri==null)throw new IllegalStateException("FileProvider export method missing");Intent send=new Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_STREAM,uri).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);send.setClipData(ClipData.newRawUri("Music diagnostic",uri));a.startActivity(Intent.createChooser(send,"Export Music report"));}catch(Throwable error){Toast.makeText(a,"Report export is unavailable; use Copy summary",Toast.LENGTH_LONG).show();}}
 private static void show(Activity a){if(shown||previous==null||a.isFinishing())return;shown=true;final String text=displaySummary(previous);try{TextView view=new TextView(a);view.setText(text);view.setTextSize(12);view.setPadding(24,24,24,24);view.setTextIsSelectable(false);ScrollView scroll=new ScrollView(a);scroll.addView(view);new AlertDialog.Builder(a).setTitle("Music startup diagnostic").setView(scroll).setPositiveButton("Copy summary",(d,w)->{android.content.ClipboardManager cm=(android.content.ClipboardManager)a.getSystemService(Context.CLIPBOARD_SERVICE);cm.setPrimaryClip(ClipData.newPlainText("Music diagnostic",text));acknowledge();}).setNeutralButton("Export file",(d,w)->exportReport(a)).setNegativeButton("Close",(d,w)->acknowledge()).show();}catch(Throwable ignored){shown=false;}}
}
