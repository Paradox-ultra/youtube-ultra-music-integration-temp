exec(open('/tmp/music-tools/audit-dex.py').read().split('orig={};new={}')[0])
import re
orig={};new={}
with zipfile.ZipFile('/downloads/music-input.apk') as z:
 for n in z.namelist():
  if n.endswith('.dex'):orig.update(parse(z.read(n)))
for d in ['shadow-dex','shadow-desugar-dex']:
 for p in pathlib.Path('/tmp/music-tools',d).glob('*.dex'):new.update(parse(p.read_bytes()))
mp={
'Landroid/app/Activity;':'Lcom/tencent/shadow/core/runtime/ShadowActivity;',
'Landroid/app/NativeActivity;':'Lcom/tencent/shadow/core/runtime/ShadowNativeActivity;',
'Landroid/app/Application;':'Lcom/tencent/shadow/core/runtime/ShadowApplication;',
'Landroid/app/Application$ActivityLifecycleCallbacks;':'Lcom/tencent/shadow/core/runtime/ShadowActivityLifecycleCallbacks;',
'Landroid/app/Service;':'Lcom/tencent/shadow/core/runtime/ShadowService;',
'Landroid/app/IntentService;':'Lcom/tencent/shadow/core/runtime/ShadowIntentService;',
'Landroid/app/AppComponentFactory;':'Lcom/tencent/shadow/core/runtime/ShadowAppComponentFactory;',
'Landroid/app/Instrumentation;':'Lcom/tencent/shadow/core/runtime/ShadowInstrumentation;'
}
def norm(s):
 for a,b in mp.items():s=s.replace(a,b)
 return s
normorig={norm(c):{(norm(m[0]),m[1],norm(m[2])) for m in ms} for c,ms in orig.items()}
missing={c:sorted(m-set(new.get(c,[]))) for c,m in normorig.items() if m-set(new.get(c,[]))}
r={'expected_classes':len(normorig),'actual_classes':len(new),'missing_classes':sorted(set(normorig)-set(new)),'new_classes':sorted(set(new)-set(normorig)),'missing_methods':missing}
pathlib.Path('/downloads/music-shadow-normalized-audit.json').write_text(json.dumps(r,indent=2))
print(json.dumps(r,indent=2)[:14000])
