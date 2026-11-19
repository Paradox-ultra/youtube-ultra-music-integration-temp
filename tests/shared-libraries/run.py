#!/usr/bin/env python3
"""Run the actual compiled plugin-loader regression with externally supplied inputs."""
import argparse, os, re, subprocess, tempfile
from pathlib import Path
p = argparse.ArgumentParser()
p.add_argument('host_jar')
p.add_argument('transformed_music_jar')
p.add_argument('android_jar')
p.add_argument('apache_optional_jar')
a = p.parse_args()
root = Path(__file__).resolve().parents[2]
inputs = [str(Path(x).resolve()) for x in (a.host_jar, a.transformed_music_jar, a.android_jar, a.apache_optional_jar)]
host, donor, android, apache = inputs
source = (root / 'native-loader/src/paradox/music/nativeui/NativeMusicActivity.java').read_text()
block = source.split('new LoadParameters("ultra_native_music", "music", null,', 1)[1].split('}).writeToParcel', 1)[0]
whitelist = re.findall(r'"([A-Za-z0-9.*_]+)"', block)
assert len(whitelist) == 9
with tempfile.TemporaryDirectory(prefix='ultra-sharedlibs-') as temp:
    tmp = Path(temp)
    classes = tmp / 'classes'
    classes.mkdir()
    compiler = ['java', str(root / 'scripts/java/Compile.java')]
    files = sorted(str(f) for f in Path(__file__).parent.rglob('*.java') if f.name != 'SharedLibraryHarness.java')
    subprocess.run(compiler + ['-source', '8', '-target', '8', '-cp', host, '-d', str(classes)] + files, check=True)
    cp = os.pathsep.join([str(classes), host])
    subprocess.run(compiler + ['-cp', cp, '-d', str(classes), str(Path(__file__).parent / 'SharedLibraryHarness.java')], check=True)
    wl = tmp / 'whitelist.txt'
    wl.write_text('\n'.join(whitelist))
    subprocess.run(['java', '-cp', os.pathsep.join([cp, android, apache]), 'SharedLibraryHarness', donor, str(wl), android], check=True)
