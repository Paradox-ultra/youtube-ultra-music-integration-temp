import java.util.*;
public class CollectionSmoke {
 public static void main(String[] a)throws Exception{
  Set<String> set=new HashSet<>();set.add("music");
  Class<?> desugar=Class.forName("j$.util.DesugarCollections");
  Object result=desugar.getMethod("unmodifiableSet",Set.class).invoke(null,set);
  if(!(result instanceof Set))throw new AssertionError("Not Set: "+result.getClass());
  Object[] arr=((Set<?>)result).toArray();if(arr==null||arr.length!=1)throw new AssertionError("Wrong array");
  System.out.println("PASS unmodifiableSet runtime type and toArray");
  Class<?> component=Class.forName("bfro");java.lang.reflect.Method factory=component.getMethod("e",Object.class,Class.class,Class[].class);factory.setAccessible(true);
  Object c=factory.invoke(null,"music",String.class,new Class[0]);java.lang.reflect.Field f=component.getField("b");f.setAccessible(true);Object value=f.get(c);
  if(!(value instanceof Set)||((Set<?>)value).toArray()==null)throw new AssertionError("Component class-set failed");
  System.out.println("PASS bfro component factory and class-set array");System.out.flush();Runtime.getRuntime().halt(0);
 }
}
