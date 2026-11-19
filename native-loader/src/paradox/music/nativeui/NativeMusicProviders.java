package paradox.music.nativeui;
import android.content.*;
import android.content.pm.*;
import com.tencent.shadow.core.loader.infos.PluginParts;
import com.tencent.shadow.core.runtime.PluginManifest;
import java.util.*;

/** Provider instances and terminal failures belong to a plugin instance, not a startup attempt. */
public final class NativeMusicProviders {
 private static final Map<Object,Map<PluginParts,Map<String,Record>>> states=new WeakHashMap<>();
 private static final class Record {ContentProvider provider;Throwable failure;boolean complete;}
 public static synchronized void create(Object owner,Context context,String part,PluginParts parts,Collection<PluginManifest.ProviderInfo> infos,Map<String,ContentProvider> providers){
  if(infos==null)return;
  if(parts==null)throw new IllegalStateException("Missing plugin parts for "+part);
  Map<PluginParts,Map<String,Record>> byParts=states.get(owner);
  if(byParts==null){byParts=new IdentityHashMap<>();states.put(owner,byParts);}
  Map<String,Record> records=byParts.get(parts);
  if(records==null){records=new HashMap<>();byParts.put(parts,records);}
  final Map<String,android.content.pm.ProviderInfo> details=new HashMap<>();
  PackageInfo pi=parts.getPluginPackageManager().getPackageInfo(context.getPackageName(),PackageManager.GET_PROVIDERS|PackageManager.GET_META_DATA);
  if(pi!=null&&pi.providers!=null)for(android.content.pm.ProviderInfo info:pi.providers)details.put(info.name,info);
  List<PluginManifest.ProviderInfo> ordered=new ArrayList<>(infos);
  Collections.sort(ordered,(a,b)->{android.content.pm.ProviderInfo x=details.get(a.className),y=details.get(b.className);int order=Integer.compare(y==null?0:y.initOrder,x==null?0:x.initOrder);return order==0?a.className.compareTo(b.className):order;});
  for(PluginManifest.ProviderInfo info:ordered){
   String key=part+"/"+info.className;
   Record record=records.get(key);
   if(record!=null){if(record.failure!=null)throw failure(part,info,record.failure);if(!record.complete)throw new IllegalStateException("Reentrant provider initialization: "+info.className);publish(providers,info,record.provider);continue;}
   record=new Record();records.put(key,record);
   try {
    for(String alias:aliases(info.authorities))if(providers.containsKey(alias))throw new IllegalStateException("Provider authority collision: "+alias);
    ContentProvider provider=parts.getAppComponentFactory().instantiateProvider(parts.getClassLoader(),info.className);
    if(provider==null)throw new IllegalStateException("Provider factory returned null: "+info.className);
    record.provider=provider;
    android.content.pm.ProviderInfo manifest=details.get(info.className);
    android.content.pm.ProviderInfo platform=manifest==null?new android.content.pm.ProviderInfo():new android.content.pm.ProviderInfo(manifest);
    platform.packageName=context.getPackageName();platform.name=info.className;platform.authority=info.authorities;platform.grantUriPermissions=info.grantUriPermissions;
    try{provider.attachInfo(context,platform);}catch(Throwable error){if(!benignMlKit(info.className,error,parts.getClassLoader()))throw error;}
    publish(providers,info,provider);record.complete=true;
   }catch(Throwable error){record.failure=error;throw failure(part,info,error);}
  }
 }
 private static List<String> aliases(String authority){List<String> out=new ArrayList<>();for(String s:authority.split(";"))if(!s.trim().isEmpty())out.add(s.trim());return out;}
 private static void publish(Map<String,ContentProvider> providers,PluginManifest.ProviderInfo info,ContentProvider provider){for(String alias:aliases(info.authorities)){ContentProvider existing=providers.get(alias);if(existing!=null&&existing!=provider)throw new IllegalStateException("Provider authority collision: "+alias);providers.put(alias,provider);}}
 private static RuntimeException failure(String part,PluginManifest.ProviderInfo info,Throwable cause){return new RuntimeException("partKey="+part+" className="+info.className+" authorities="+info.authorities,cause);}
 private static boolean benignMlKit(String name,Throwable error,ClassLoader loader){
  if(!"com.google.mlkit.common.internal.MlKitInitProvider".equals(name))return false;
  if(!(error instanceof IllegalStateException)||!"MlKitContext is already initialized".equals(error.getMessage()))return false;
  try {Class<?> singleton=Class.forName("bghx",false,loader);Object lock=singleton.getField("a").get(null);synchronized(lock){Object value=singleton.getField("b").get(null);return value!=null&&singleton.getField("c").get(value)!=null;}}catch(Throwable invalid){return false;}
 }
}
