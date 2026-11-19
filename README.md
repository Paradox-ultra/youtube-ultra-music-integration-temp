# YouTube Ultra native Music integration — source recovery

Experimental source/patch recovery. This repository does not claim that Music Library, same-account login or playback work end to end.

## Current source state

The latest documented delivered APK is BqoDF89d. The test device has an unresolved Music Application startup ANR. See [BUILD-MAP.md](BUILD-MAP.md), [REBUILD.md](REBUILD.md), and [UPSTREAM.md](UPSTREAM.md).

The source hardening patch dated 2026-11-19 removes a loader lock held across main-thread plugin Application startup and makes extraction recovery more defensive. This is not a validated ANR fix. Android Application lifecycle callbacks still execute on the main thread; moving Application.onCreate to a worker thread would be unsafe.

## Contents

- `native-loader/src`: custom native loader, account bridge, container and diagnostic source.
- `patches/java`: bytecode patch utilities.
- `scripts`: DEX restoration, assembly, transformation, resource rebasing and audit helpers. Legacy scripts preserve development-layout assumptions.
- `shadow-source`: pinned Tencent Shadow SDK source and its license.
- `host-deltas`: selected host integration snapshots, not a complete YouTube source distribution.
- `tests`: synthetic JVM/ART fixtures and generators, not device tests.
- `previous-module`: earlier separate-module checkpoint, retained for recovery only.

## Rebuild and release constraints

A full clean-room build from a Git clone has not been demonstrated. Supply the original host and Music APK inputs documented in UPSTREAM.md, pinned dependencies, and Android SDK/build tools separately. Supply your own signing key locally; no signing material is stored here. Re-signing changes install/update identity.

Before distributing an APK, verify signatures, DEX references, providers, account selection, Library rendering, playback controls, media session, background playback, process restarts and Android-version compatibility on physical devices.

See [SOURCE-HARDENING-2026-11-19.md](SOURCE-HARDENING-2026-11-19.md) for source changes and verification limits.
