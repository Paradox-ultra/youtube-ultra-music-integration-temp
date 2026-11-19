# YouTube Ultra native Music integration, temporary source recovery

Experimental source/patch recovery. This is not a stable release and not a claim that Music Library, same-account login or playback works end to end.

This repository was created after the APKs were built. Its commits reconstruct patch stages from preserved source and artifacts; they are not the original build-time Git history. `BUILD-MAP.md` maps the delivered APK identifiers and hashes to the relevant recovery commits. The latest phone build is BqoDF89d; investigation of an Application startup ANR is still open.

No APKs, signing material, credentials, personal data, device reports or private logs are included. Use your own signing key locally. The main youtube-ultra repository was not changed.

## Contents

- `native-loader/src`: all custom native loader, account bridge, container and diagnostics Java source for the latest delivered build.
- `patches/java`: bytecode patch utilities for the signature adapter, provider manager, SslGuard listener and exit diagnostics.
- `scripts`: DEX name restoration, assembly, transformation, resource rebasing and audit helpers used during development. The `legacy/` scripts preserve earlier path-specific preparation steps and are not a one-command clean builder.
- `shadow-source`: exact Tencent Shadow SDK modules used, pinned in `UPSTREAM.md`, with its license.
- `host-deltas`: selected manifest, entry/icon, account-hook and signer-guard source snapshots from the modified host. They are integration deltas, not a complete YouTube source distribution.
- `tests`: synthetic JVM/ART test source and fixture generators, not phone logs.
- `previous-module`: source from the earlier separate integrated-module checkpoint, retained for recovery and clearly separate from the current embedded loader.

## Rebuild scope and honest limits

See `REBUILD.md`. Source for every custom runtime class and every current patch is included. Exact original APKs and third-party build dependencies must be supplied separately. A complete clean-room, bit-for-bit rebuild from only a Git clone has not been demonstrated. Some conversion/preparation scripts use the documented development layout; scripts are preserved for recovery rather than falsely advertised as a portable full build. No private key or donor binary is required to read or review this repository.

The current build freezes during Music Application startup on the test device. Core Application startup enforces Android's main thread. Moving it wholesale to a worker is not a validated fix. Unfinished timing instrumentation is kept separately under `work-in-progress/` and is not part of BqoDF89d.
