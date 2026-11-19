import zipfile,pathlib,shutil
root=pathlib.Path('/tmp/ultra-public-current');existing={str(p.relative_to(d))[:-6]+'.class' for d in root.glob('smali*') for p in d.rglob('*.smali')}
with zipfile.ZipFile('/tmp/music-native-loader/host-runtime.jar','w',zipfile.ZIP_DEFLATED) as out:
 for base in ['common','runtime','loader','activity-container','load-parameters']:
  with zipfile.ZipFile('/tmp/music-shadow/projects/sdk/core/'+base+'/build/libs/'+base+'.jar') as z:
   for n in z.namelist():
    if n.endswith('.class'):out.writestr(n,z.read(n))
 for p in pathlib.Path('/tmp/music-native-loader/classes').rglob('*.class'):out.write(p,str(p.relative_to('/tmp/music-native-loader/classes')))
 for path in pathlib.Path('/tmp/music-tools/gradle-7.5/lib').glob('kotlin-stdlib*.jar'):
  with zipfile.ZipFile(path) as z:
   for n in z.namelist():
    if n.endswith('.class') and n not in existing and not n.startswith('META-INF/'):
     out.writestr(n,z.read(n));existing.add(n)
print('Host runtime prepared')
