package paradox.music.nativeui;
import android.content.Context;import android.content.ContextWrapper;
/** Keeps the original service base facilities but exposes its owning plugin Application. */
public final class NativeMusicComponentContext {
 public static Context serviceBase(Context base,Context application){
  if(application==null)throw new IllegalStateException("Music component Application missing before attach");
  if(base.getApplicationContext()==application)return base;
  return new ContextWrapper(base){@Override public Context getApplicationContext(){return application;}};
 }
}
