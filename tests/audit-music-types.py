import struct,zipfile,json,collections
from pathlib import Path
def dex(b):
 u=lambda o:struct.unpack_from('<I',b,o)[0]
 def st(off):
  while b[off]&128:off+=1
  off+=1;return b[off:b.index(0,off)].decode('utf8','replace')
 ss=[st(u(u(60)+4*i)) for i in range(u(56))];ts=[ss[u(u(68)+4*i)] for i in range(u(64))]
 defs={ts[u(u(100)+32*i)] for i in range(u(96))}
 return defs,{t.lstrip('[') for t in ts if t.lstrip('[').startswith('L')}
def apk(p):
 ds=set();rs=set()
 with zipfile.ZipFile(p) as z:
  for n in z.namelist():
   if n.endswith('.dex'):
    d,r=dex(z.read(n));ds|=d;rs|=r
 return ds,rs
if __name__=='__main__':
 orig,orr=apk('/downloads/Music-Ultra-Bound-Outline-Icons.apk');cur,cr=apk('/tmp/ultra-selftest/exact-embedded-phone.apk')
 for name,d,r in [('approved',orig,orr),('embedded',cur,cr)]:
  miss=sorted(r-d);j=[c for c in miss if c.startswith('Lj$/')];Path('/tmp/ultra-selftest/'+name+'-missing-types.json').write_text(json.dumps(miss,indent=2));print(name,'defs',len(d),'refs',len(r),'unresolved',len(miss),'j$ unresolved',len(j));print('\n'.join(j));print('other non-platform',collections.Counter(x.split('/')[0] for x in miss))
 print('new unresolved vs original:', '\n'.join(sorted((cr-cur)-(orr-orig))))
