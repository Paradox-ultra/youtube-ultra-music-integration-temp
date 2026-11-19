#!/usr/bin/env python3
"""Run synthetic HTTP400 privacy and non-mutation tests against the exact final host JAR."""
import argparse, os, subprocess, tempfile
from pathlib import Path
p=argparse.ArgumentParser()
p.add_argument('final_host_jar')
p.add_argument('android_jar')
a=p.parse_args()
root=Path(__file__).resolve().parents[2]
host=str(Path(a.final_host_jar).resolve())
android=str(Path(a.android_jar).resolve())
with tempfile.TemporaryDirectory(prefix='ultra-http400-privacy-') as temp:
    files=sorted(str(f) for f in (root/'tests/network-privacy').rglob('*.java'))
    files.append(str(Path(__file__).parent/'Http400PrivacyHarness.java'))
    subprocess.run(['java',str(root/'scripts/java/Compile.java'),'-cp',os.pathsep.join([host,android]),'-d',temp]+files,check=True)
    for harness in ['NetworkPrivacyHarness','Http400PrivacyHarness']:
        subprocess.run(['java','-cp',os.pathsep.join([temp,host,android]),harness],check=True)
