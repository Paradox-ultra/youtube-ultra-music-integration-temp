import java.util.*;import java.util.jar.*;import java.nio.file.*;import java.io.*;import javassist.bytecode.ClassFile;
public class ShadeHostKotlin {
 public static void main(String[]args)throws Exception {
  Map<String,byte[]> classes=new LinkedHashMap<>();
  List<String> paths=new ArrayList<>();paths.add("/tmp/music-native-loader/host-runtime.jar");
  try(var ps=Files.list(Paths.get("/tmp/music-tools/gradle-7.5/lib"))){ps.filter(p->p.getFileName().toString().startsWith("kotlin-stdlib")&&p.toString().endsWith(".jar")).forEach(p->paths.add(p.toString()));}
  for(String path:paths)try(JarFile jar=new JarFile(path)){var es=jar.entries();while(es.hasMoreElements()){var e=es.nextElement();if(e.getName().endsWith(".class")&&!e.getName().startsWith("META-INF/"))classes.put(e.getName(),jar.getInputStream(e).readAllBytes());}}
  Map<String,String> names=new HashMap<>();for(String name:classes.keySet())if(name.startsWith("kotlin/")){String old=name.substring(0,name.length()-6);names.put(old,"paradox/music/shaded/"+old);}
  int count=0;
  try(JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get("/tmp/music-native-loader/host-runtime-shaded.jar")))){
   for(var entry:classes.entrySet()){
    ClassFile c=new ClassFile(new DataInputStream(new ByteArrayInputStream(entry.getValue())));c.renameClass(names);
    if(c.getName().equals("com.tencent.shadow.core.runtime.container.PluginContainerActivity"))for(javassist.bytecode.MethodInfo m:c.getMethods())if(m.getName().equals("onCreate"))m.setAccessFlags(m.getAccessFlags() & ~javassist.bytecode.AccessFlag.FINAL);
    ByteArrayOutputStream bytes=new ByteArrayOutputStream();c.write(new DataOutputStream(bytes));
    out.putNextEntry(new JarEntry(c.getName().replace('.','/')+".class"));out.write(bytes.toByteArray());out.closeEntry();count++;
   }
  }
  System.out.println("Relocated Kotlin classes: "+names.size()+"; total host runtime classes: "+count);
 }
}
