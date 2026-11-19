from pathlib import Path
import zipfile,shutil
host=Path('/tmp/ultra-public-current')
# Side-by-side package fallback keeps the visible app name intact.
for p in host.rglob('*'):
 if p.suffix in ('.smali','.xml','.yml') and p.is_file():
  try:s=p.read_text()
  except UnicodeDecodeError:continue
  n=s.replace('app.paradox.youtube','app.paradox.ultra02')
  if n!=s:p.write_text(n)
p=host/'smali_classes4/wkm.smali';s=p.read_text();start=s.index('.method public constructor <init>');i=s.index('    invoke-direct {p0}, Ljava/lang/Object;-><init>()V',start);i=s.index('\n',i)+1;s=s[:i]+'\n    invoke-static {p1}, Lparadox/music/nativeui/AccountBridge;->bindHost(Landroid/content/SharedPreferences;)V\n'+s[i:];p.write_text(s)
p=host/'smali_classes3/gqg.smali';s=p.read_text();needle='    invoke-virtual {v7}, Lbds;->u()Z';i=s.index(needle);s=s[:i]+'''    invoke-virtual {v7}, Landroid/view/View;->getContext()Landroid/content/Context;
    move-result-object v8
    invoke-static {v8}, Lparadox/music/nativeui/NativeMusicActivity;->open(Landroid/content/Context;)V
    return-void

'''+s[i:];p.write_text(s)
for name in ['yt_outline_compass_black_24.xml','yt_outline_compass_cairo_black_24.xml']:
 target=host/'res/drawable'/name
 shutil.copy(host/'res/drawable/yt_outline_music_vd_theme_24.xml',target)
 for raster in (host/'res').glob('drawable-*/'+name.replace('.xml','.png')):raster.unlink()
p=host/'AndroidManifest.xml';s=p.read_text();extras='''
        <activity android:name="paradox.music.nativeui.NativeMusicActivity" android:exported="false" android:theme="@android:style/Theme.Material.NoActionBar" />
        <activity android:name="paradox.music.nativeui.MusicTaskActivity" android:exported="false" android:launchMode="singleTask" android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|screenSize|smallestScreenSize|uiMode" android:hardwareAccelerated="true" />
        <activity android:name="paradox.music.nativeui.MusicProxyActivity" android:exported="false" android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|screenSize|smallestScreenSize|uiMode" android:hardwareAccelerated="true" />
        <provider android:name="com.tencent.shadow.core.runtime.container.PluginContainerContentProvider" android:authorities="app.paradox.ultra02.native_music.provider" android:exported="false" android:grantUriPermissions="true" />
        <service android:name="paradox.music.nativeui.MusicProxyService" android:exported="false" android:foregroundServiceType="mediaPlayback|dataSync|connectedDevice" />
''';s=s.replace('</application>',extras+'</application>');p.write_text(s)
print('Prepared public host package, native Music entry, drawer bypass, icon and identity hooks')
