import zipfile,pathlib,shutil
out='/tmp/music-native-loader/music-plugin-final.apk'
with zipfile.ZipFile('/tmp/music-tools/music-plugin-resources.apk') as z,zipfile.ZipFile(out,'w',zipfile.ZIP_DEFLATED) as w:
 for n in z.namelist():
  if not (n.endswith('.dex') or n.startswith('META-INF/')):w.writestr(n,z.read(n))
 ds=sorted(pathlib.Path('/tmp/music-tools/shadow-dex').glob('*.dex'))+sorted(pathlib.Path('/tmp/music-tools/shadow-desugar-dex').glob('*.dex'))+list(pathlib.Path('/tmp/music-native-loader/metadata-dex').glob('*.dex'))
 for i,p in enumerate(ds,1):w.write(p,'classes'+('' if i==1 else str(i))+'.dex')
dest=pathlib.Path('/tmp/ultra-public-current/assets/ultra_music/music.apk');dest.parent.mkdir(parents=True,exist_ok=True);shutil.copy(out,dest)
print('Embedded native Music payload:',pathlib.Path(out).stat().st_size)
