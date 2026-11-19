#!/usr/bin/env python3
"""Privacy and metadata-only observation fixture. Inputs contain no real user data."""
import argparse, os, subprocess, tempfile
from pathlib import Path
p=argparse.ArgumentParser()
p.add_argument('final_host_jar')
p.add_argument('android_jar')
a=p.parse_args()
root=Path(__file__).resolve().parents[2]
host=str(Path(a.final_host_jar).resolve())
android=str(Path(a.android_jar).resolve())
with tempfile.TemporaryDirectory(prefix='ultra-network-privacy-') as temp:
    files=sorted(str(f) for f in Path(__file__).parent.rglob('*.java'))
    subprocess.run(['java',str(root/'scripts/java/Compile.java'),'-cp',os.pathsep.join([host,android]),'-d',temp]+files,check=True)
    subprocess.run(['java','-cp',os.pathsep.join([temp,host,android]),'NetworkPrivacyHarness'],check=True)
