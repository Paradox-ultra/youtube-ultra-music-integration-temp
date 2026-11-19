import java.util.*;
import java.lang.reflect.*;
import dalvik.system.DexFile;
public final class ClassLoadSmoke {
 public static void main(String[] args) throws Exception {
  int total=0,loaded=0,members=0,failed=0;
  DexFile dex=new DexFile(args[0]);
  Enumeration<String> names=dex.entries();
  while(names.hasMoreElements()) {
   String name=names.nextElement();total++;
   try {
    Class<?> c=Class.forName(name,false,ClassLoadSmoke.class.getClassLoader());loaded++;
    c.getDeclaredConstructors();c.getDeclaredMethods();c.getDeclaredFields();members++;
   } catch(Throwable error) {
    failed++;System.out.println("FAIL "+name+" "+error);
   }
  }
  System.out.println("CLASSLOAD total="+total+" loaded="+loaded+" members="+members+" failed="+failed);
  System.out.flush();Runtime.getRuntime().halt(failed==0?0:1);
 }
}
