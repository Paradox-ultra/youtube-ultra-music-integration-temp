import javassist.*;
import com.tencent.shadow.core.transform.*;
import com.tencent.shadow.core.transform_kit.*;
import kotlin.jvm.functions.Function0;
import java.util.*;
import java.util.jar.*;
import java.nio.file.*;
public class TransformDonor {
 public static void main(String[] args) throws Exception {
  AutoMakeMissingClassPool pool = new AutoMakeMissingClassPool(false);
  pool.appendClassPath("/tmp/music-tools/platform33/android-13/android.jar");
  for(String path:args[0].split(":"))pool.appendClassPath(path);
  Set<CtClass> classes = new LinkedHashSet<>();
  List<Path> jars = new ArrayList<>();
  try(var paths=Files.list(Paths.get("/tmp/music-tools/donor-dex"))){paths.filter(p->p.toString().endsWith(".jar")).sorted().forEach(jars::add);}
  List<ClassPath> donorPaths = new ArrayList<>();
  for(Path path:jars)donorPaths.add(pool.insertClassPath(path.toString()));
  for(Path path:jars)try(JarFile jar = new JarFile(path.toFile())) {
   var en=jar.entries();while(en.hasMoreElements()) {
    String name=en.nextElement().getName();if(name.endsWith(".class") && !name.equals("android/app/AppComponentFactory.class"))classes.add(pool.makeClass(jar.getInputStream(jar.getJarEntry(name))));
   }
  }
  for(String line:Files.readAllLines(Paths.get("/tmp/music-tools/donor-name-restoration.tsv"))) { String[] pair=line.split("\t"); pool.get(pair[0]).setName(pair[1]); }
  for(CtClass clazz:classes) { clazz.getClassFile().setMajorVersion(49); for(var mi:clazz.getClassFile2().getMethods()) { var ca=mi.getCodeAttribute(); if(ca!=null) ca.setMaxStack(Math.max(ca.getMaxStack()+32,ca.computeMaxStack())); } }
  for(CtClass clazz:classes) if(clazz.getName().equals("com")) clazz.setName("__shadow_obfuscated_com");
  for(ClassPath path:donorPaths)pool.removeClassPath(path);
  System.out.println("LOADED_CLASSES "+classes.size());
  pool.makeInterface("com.tencent.shadow.core.transform.SelfClassNamePlaceholder");
  System.out.println("RUNTIME_CLASS "+pool.get("com.tencent.shadow.core.runtime.PackageManagerInvokeRedirect"));
  TransformManager manager=new TransformManager(pool,()->new String[0]);
  manager.setupAll(classes);
  System.out.println("SETUP_COMPLETE");
  for(SpecificTransform transform:manager.getMTransformList()) {
   System.out.println("TRANSFORM "+transform.getClass().getSimpleName());
   for(TransformStep step:transform.getList()) {
    Set<CtClass> affected=step.filter(classes);System.out.println("STEP_CLASSES "+affected.size());
    for(CtClass clazz:affected) {
     if(transform.getClass().getSimpleName().equals("InstrumentationTransform") && !clazz.getRefClasses().contains("com.tencent.shadow.core.runtime.ShadowInstrumentation") && !clazz.getRefClasses().contains("android.app.Instrumentation")) continue;
     clazz.getClassFile().setMajorVersion(49);
     step.transform(clazz);
    }
   }
  }
  for(CtClass clazz:classes) if(clazz.getName().equals("__shadow_obfuscated_com")) clazz.setName("com");
  try(JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get("/tmp/music-tools/music-shadow-transformed.jar")))) {
   for(CtClass clazz:classes){out.putNextEntry(new JarEntry(clazz.getName().replace('.','/')+".class"));out.write(clazz.toBytecode());out.closeEntry();}
  }
  System.out.println("TRANSFORM_COMPLETE");
 }
}
