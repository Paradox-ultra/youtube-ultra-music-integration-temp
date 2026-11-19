package paradox.music.nativeui;
import java.security.*;
import javax.net.ssl.*;

/** Reuses process-wide Ultra SslGuard only after proving it is the original initialized guard. */
public final class NativeMusicSslGuard {
 public static boolean reuseHostGuard(){
  Provider existing=Security.getProvider("Ssl_Guard");
  if(existing==null)return false;
  try{
   ClassLoader host=NativeMusicSslGuard.class.getClassLoader();
   Class<?> type=Class.forName("uvc",false,host);
   if(existing.getClass()!=type)throw new IllegalStateException("Existing Ssl_Guard is not Ultra's provider");
   Object lock=Class.forName("uuy",false,host).getField("a").get(null);
   synchronized(lock){
    if(Class.forName("uuy",false,host).getField("b").get(null)!=existing)throw new IllegalStateException("SslGuard provider state differs from installed provider");
    Provider[] providers=Security.getProviders();
    if(providers.length==0||providers[0]!=existing)throw new IllegalStateException("Ultra SslGuard is not at top priority");
    if(Class.forName("akpm",false,host).getField("c").get(null)==null)throw new IllegalStateException("Ultra SslGuard has no initialization context");
    for(String name:new String[]{"uve","uvd"})if(Class.forName(name,false,host).getField("a").get(null)==null)throw new IllegalStateException("Ultra SslGuard callback missing: "+name);
    Class<?> spi=Class.forName("uuz",false,host);
    for(String protocol:new String[]{"SSL","SSLv3","TLS","TLSv1","TLSv1.1","TLSv1.2","Default"}){
     Provider.Service service=existing.getService("SSLContext",protocol);
     if(service==null||!spi.getName().equals(service.getClassName()))throw new IllegalStateException("Unexpected Ultra SSL service: "+protocol);
    }
    SSLContext context=SSLContext.getInstance("TLS",existing);context.init(null,null,null);
    if(!Class.forName("uve",false,host).isInstance(context.getSocketFactory()))throw new IllegalStateException("Ultra guard socket factory missing");
    return true;
   }
  }catch(Exception e){throw new IllegalStateException("Cannot reuse existing process SslGuard safely",e);}
 }
}
