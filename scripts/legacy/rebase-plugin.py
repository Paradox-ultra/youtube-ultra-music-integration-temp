import zipfile,struct,pathlib,xml.etree.ElementTree as ET,json
root=ET.parse('/tmp/music-preserved-search/res/values/public.xml').getroot()
ids={int(e.attrib['id'],16) for e in root}
count=0;strcount=0

def patch(d):
 global count,strcount
 n=struct.unpack_from('>H',d,8)[0];o=10;i=1;out=bytearray(d[:10])
 while i<n:
  start=o;t=d[o];o+=1;out.append(t)
  if t==1:
   l=struct.unpack_from('>H',d,o)[0];o+=2;b=d[o:o+l];o+=l
   c=b.replace(b'app.rvx.android.apps.youtube.musix',b'app.paradox.youtubeintegrated').replace(b'app.rvx.android.apps.youtube.music',b'app.paradox.youtubeintegrated')
   strcount+=(b!=c);out+=struct.pack('>H',len(c))+c
  elif t==3:
   v=struct.unpack_from('>I',d,o)[0];o+=4
   if v in ids:v=(v&0xffffff)|0x80000000;count+=1
   out+=struct.pack('>I',v)
  else:
   length={4:4,5:8,6:8,7:2,8:2,9:4,10:4,11:4,12:4,15:3,16:2,17:4,18:4,19:2,20:2}[t]
   out+=d[o:o+length];o+=length
   if t in (5,6):i+=1
  i+=1
 out+=d[o:];return bytes(out)
with zipfile.ZipFile('/tmp/music-tools/music-shadow-transformed.jar') as z,zipfile.ZipFile('/tmp/music-tools/music-plugin-rebased.jar','w',compression=zipfile.ZIP_DEFLATED) as a,zipfile.ZipFile('/tmp/music-tools/music-plugin-desugar.jar','w',compression=zipfile.ZIP_DEFLATED) as b:
 for n in z.namelist():(b if n.startswith('j$/') else a).writestr(n,patch(z.read(n)))
print(json.dumps({'resource_constants_rebased':count,'package_strings_rebased':strcount}))
# This input is for metadata generation, not a mutation of the preserved standalone.
p=pathlib.Path('/tmp/music-plugin-resources/AndroidManifest.xml');s=p.read_text()
for name,val in {'standard':0,'singleTop':1,'singleTask':2,'singleInstance':3,'singleInstancePerTask':4}.items():s=s.replace('android:launchMode="'+name+'"','android:launchMode="'+str(val)+'"')
pathlib.Path('/tmp/music-native-loader/AndroidManifest-metadata.xml').write_text(s)
