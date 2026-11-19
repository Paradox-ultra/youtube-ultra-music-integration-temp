from pathlib import Path
import zipfile,struct,hashlib,zlib,json
src=Path('/downloads/Music-Ultra-Catalog-Search.apk');out=Path('/tmp/music-tools/Music-Ultra-Safe-Online-unsigned.apk')
with zipfile.ZipFile(src) as z:d=bytearray(z.read('classes3.dex'))
# Absolute byte offsets are from dexdump of the last launching APK, not a rebuilt DEX.
patches=[
 (0x671794,bytes.fromhex('38000300'),bytes.fromhex('29000300'),'SearchInputFragment.m: enter suggestions after native metadata check'),
 (0x6742c8,bytes.fromhex('39004300'),bytes.fromhex('29004300'),'SearchResultFragment.d: bypass automatic Downloads override'),
 (0x6743bc,bytes.fromhex('39002001'),bytes.fromhex('00000000'),'SearchResultFragment.d: continue native online request instead of local-only route')
]
for off,old,new,label in patches:
 assert d[off:off+len(old)]==old,(hex(off),label)
 assert len(old)==len(new)
 d[off:off+len(old)]=new
# Preserve every code size, register count, string/type/method table, debug item and offset.
d[12:32]=hashlib.sha1(d[32:]).digest();struct.pack_into('<I',d,8,zlib.adler32(d[12:])&0xffffffff)
Path('/tmp/music-tools/safe-online-classes3.dex').write_bytes(d)
with zipfile.ZipFile(src) as z,zipfile.ZipFile(out,'w') as a:
 for item in z.infolist():
  if item.filename.startswith('META-INF/'):continue
  a.writestr(item,d if item.filename=='classes3.dex' else z.read(item.filename))
print(json.dumps({'source':str(src),'output':str(out),'patches':[{'offset':hex(o),'before':a.hex(),'after':b.hex(),'scope':c} for o,a,b,c in patches]},indent=2))
