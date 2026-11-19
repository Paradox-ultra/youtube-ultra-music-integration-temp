import javassist.*;import javassist.expr.*;import java.util.*;import java.util.jar.*;import java.nio.file.*;
/** Nullable activity observation and complete delegate detachment for failed initialization. */
public class PatchActivityLifecycleSafety {
 public static void main(String[]a)throws Exception {
  ClassPool p=new ClassPool(false);p.appendClassPath(a[0]);p.appendClassPath("/tmp/music-tools/platform33/android-13/android.jar");Map<String,byte[]> changes=new HashMap<>();
  CtClass d=p.get("com.tencent.shadow.core.loader.delegates.ShadowActivityDelegate");d.getDeclaredMethod("getPluginActivity").setBody("{return this.pluginActivity;}");
  final int[] stages={0};d.getDeclaredMethod("onCreate").instrument(new ExprEditor(){public void edit(MethodCall m)throws CannotCompileException{
   String name=m.getMethodName();String phase=null;
   if(name.equals("inject"))phase="Music activity dependency injection";
   else if(name.equals("instantiateActivity"))phase="Music activity construction";
   else if(name.equals("initPluginActivity"))phase="Music activity context attach and theme";
   else if(name.equals("onCreate"))phase="Music donor activity onCreate";
   if(phase!=null){m.replace("{paradox.music.nativeui.NativeMusicDiagnostics.phase(\""+phase+"\"); $_=$proceed($$);}");stages[0]++;}
  }});
  if(stages[0]!=4)throw new AssertionError("Expected four startup stage calls, got "+stages[0]);changes.put(d.getName().replace('.','/')+".class",d.toBytecode());
  CtClass c=p.get("com.tencent.shadow.core.runtime.container.PluginContainerActivity");c.addMethod(CtNewMethod.make("public final void ultraDetachFailedActivity(){super.hostActivityDelegate=null;this.hostActivityDelegate=null;}",c));changes.put(c.getName().replace('.','/')+".class",c.toBytecode());
  try(JarFile z=new JarFile(a[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(a[1])))){var es=z.entries();while(es.hasMoreElements()){var e=es.nextElement();out.putNextEntry(new JarEntry(e.getName()));out.write(changes.containsKey(e.getName())?changes.get(e.getName()):z.getInputStream(e).readAllBytes());out.closeEntry();}}
  System.out.println("Patched nullable shared activity resolver, both failed delegates, four first-failure stage markers");
 }
}
