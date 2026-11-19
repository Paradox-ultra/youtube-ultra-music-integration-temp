# CFH18AoO build notes

This build merges source hardening commit 23673b03737a6fa19fea8b5d45c0310ed4b4a83c with the included newer diagnostics and external-service routing patch. ChatGPT changes remain intact. The only merge overlap was retaining phase timestamps around the main-thread Application call.

A captured startup stack located main at iwy.a DEX PC 490 (0x1ea), acquiring the Google API service-connection map monitor. Another thread held that monitor while waiting for Shadow PluginServiceManager to post an unbind lookup to main. The raw dump is intentionally not published. DEX-PC mapping is needed because these transformed methods have no source debug table.

The fix records successful external framework binds in ShadowContext and sends their matching unbind directly to Android. It avoids an unnecessary plugin lookup which synchronously waits for main. Real plugin services retain their normal main-thread lifecycle path. No donor listener is skipped or moved off-main. No latch wait is silently timed out. The provisional asynchronous Google Handler-migration experiment is not included.

Diagnostics recover Android ANR trace streams off-main, write phase state off-main, and sample main and other threads every 500 ms during the first 20 seconds of startup, capped at 4 MB. These are private local device files, never automatically uploaded.

## Verification

- Current sources compile against Android 33 and pinned Shadow runtime.
- Patched synthetic fixtures cover explicit and implicit external binding/unbinding, preserved plugin routing, failed binds and context ownership.
- Diagnostic fixtures cover retained Android ANR trace recovery, main-stack sampling and ordered state cleanup.
- ART final host: 1,133 classes, 1,132 load and reflect, one pre-existing unused NativeActivity missing-class baseline.
- Embedded Music: 62,946 classes, 62,934 load, 62,887 reflect, unchanged 59 optional/platform baseline misses and no new failures.
- Same package app.paradox.youtubeintegrated, label YouTube Ultra, and same private signing certificate as prior updates.
- v2/v3 signatures, ZIP CRC, stored resources.arsc and 16 KB alignment pass. Only classes15.dex changes from z1S9S64v; Music payload is byte-identical.

These checks do not establish physical-device installation, successful startup, authentication, Library rendering or playback. This is a corrected build awaiting phone validation, not a verified finished release.

No private key, credentials, device logs or donor APKs are included. Existing recovery/build-input caveats still apply.
