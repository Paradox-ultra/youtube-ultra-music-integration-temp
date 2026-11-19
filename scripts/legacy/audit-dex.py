import struct,zipfile,json,pathlib

def parse(data):
 u=lambda o:struct.unpack_from('<I',data,o)[0]
 ns,so=u(56),u(60); nt,to=u(64),u(68); nm,mo=u(88),u(92); nc,co=u(96),u(100)
 def leb(pos):
  val=0;shift=0
  while True:
   b=data[pos];pos+=1;val|=(b&127)<<shift
   if b<128:return val,pos
   shift+=7
 def string(off):
  _,p=leb(off);return data[p:data.index(0,p)].decode('utf8',errors='replace')
 strings=[string(u(so+4*i)) for i in range(ns)]
 types=[strings[u(to+4*i)] for i in range(nt)]
 ps,po=u(72),u(76)
 protos=[]
 for i in range(ps):
  _,r,off=struct.unpack_from('<III',data,po+12*i)
  args=[] if not off else [types[struct.unpack_from('<H',data,off+4+j*2)[0]] for j in range(u(off))]
  protos.append('('+''.join(args)+')'+types[r])
 methods=[]
 for i in range(nm):
  cl,pr,na=struct.unpack_from('<HHI',data,mo+8*i);methods.append((types[cl],strings[na],protos[pr]))
 out={}
 for i in range(nc):
  ix,flags,sup,interfaces,source,annotations,cd,sv=struct.unpack_from('<8I',data,co+32*i)
  defined=[]
  if cd:
   sf,cd=leb(cd);inf,cd=leb(cd);dm,cd=leb(cd);vm,cd=leb(cd)
   for _ in range(sf+inf):a,cd=leb(cd);b,cd=leb(cd)
   for n in [dm,vm]:
    ixm=0
    for _ in range(n):
     diff,cd=leb(cd);flagsm,cd=leb(cd);code,cd=leb(cd);ixm+=diff;defined.append(methods[ixm])
  out[types[ix]]=defined
 return out
orig={};new={}
with zipfile.ZipFile('/downloads/music-input.apk') as z:
 for n in z.namelist():
  if n.endswith('.dex'):orig.update(parse(z.read(n)))
for d in ['shadow-dex','shadow-desugar-dex']:
 for p in pathlib.Path('/tmp/music-tools',d).glob('*.dex'):new.update(parse(p.read_bytes()))
missing={c:sorted(set(m)-set(new.get(c,[]))) for c,m in orig.items() if set(m)-set(new.get(c,[]))}
report={'original_classes':len(orig),'transformed_classes':len(new),'missing_classes':sorted(set(orig)-set(new)),'new_classes':sorted(set(new)-set(orig)),'missing_methods':missing,'original_defined_methods':sum(map(len,orig.values())),'transformed_defined_methods':sum(map(len,new.values()))}
pathlib.Path('/downloads/music-shadow-member-audit.json').write_text(json.dumps(report,indent=2))
print(json.dumps({k:v for k,v in report.items() if k not in ['new_classes','missing_classes','missing_methods']},indent=2))
print('missing_classes',report['missing_classes'][:30]);print('new_classes',report['new_classes'][:30]);print('missing_methods_classes',len(missing));print(list(missing.items())[:12])
