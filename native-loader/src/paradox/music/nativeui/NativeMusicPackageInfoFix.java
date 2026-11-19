package paradox.music.nativeui;
import android.content.pm.*;
import android.os.Build;
/** Embedded APKs are not installed packages and may be unsigned. Use real installed identity. */
public final class NativeMusicPackageInfoFix {
 public static PackageInfo merge(PackageManager manager,String archive,int flags,PackageInfo host,ApplicationInfo app){
  if(host==null)throw new IllegalStateException("Installed host PackageInfo is missing");
  int metadataFlags=flags & ~PackageManager.GET_SIGNATURES & ~0x08000000;
  PackageInfo value=manager.getPackageArchiveInfo(archive,metadataFlags);
  if(value==null)value=copy(host);
  value.applicationInfo=app;
  value.permissions=host.permissions;
  value.requestedPermissions=host.requestedPermissions;
  value.requestedPermissionsFlags=host.requestedPermissionsFlags;
  value.gids=host.gids;
  value.firstInstallTime=host.firstInstallTime;
  value.lastUpdateTime=host.lastUpdateTime;
  value.signatures=host.signatures;
  if(Build.VERSION.SDK_INT>=28)value.signingInfo=host.signingInfo;
  return value;
 }
 private static PackageInfo copy(PackageInfo host){
  android.os.Parcel p=android.os.Parcel.obtain();try{host.writeToParcel(p,0);p.setDataPosition(0);return PackageInfo.CREATOR.createFromParcel(p);}finally{p.recycle();}
 }
}
