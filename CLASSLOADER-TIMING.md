# Classloader fast path and attempt timing

Build xNn98Pka, SHA256 `dd3581ae164de25f88b4fcac17147d56f6cfeceb982b5a34d4cc780aed4ca921`.

This is not phone-tested. One RUNNABLE phone snapshot inside the classloader does not establish the entire later startup interval or rule out another stall. No measured phone-speed improvement is claimed.

## Classloader change

56,419 of the payload's 62,946 definitions have no package separator. For standalone plugins only (non-null specialClassLoader), PluginClassLoader now checks findLoadedClass then tries findClass immediately for a default-package name. This removes the repeated Kotlin package-string/trie path and failed boot-parent lookup for an owned obfuscated Music class. On ClassNotFoundException it falls back to the original method. The dependent-plugin branch and every packaged name, including platform, host whitelist and Shadow runtime names, retain the original routing. No negative cache, compiler/hidden API, bulk initialization, new account or alternate identity is introduced.

Apply PatchClassLoaderFastPath to the merged sweep host/runtime JAR after the earlier startup and service-routing patches and before D8. Compile the matching NativeMusicClassLoading helper first. The original findLoadedClass positive cache remains; this does not claim to add a cache Android lacked.

## Diagnostics

- Semantic build ID `music-fastpath-20261119`, per-attempt ID, elapsed phase and classloader counters.
- Last main-thread sample is overwritten every 500ms and placed before bulky exit history. A separate persisted per-attempt state is refreshed while startup is active, up to 120 seconds. The log remains bounded; only the main stack is sampled in the hot loop.
- Prior-session data is explicitly labeled. Current last-attempt state and latest sample precede a retained older exception. Acknowledgement never removes an active attempt marker. The last-attempt record survives successful completion and report acknowledgement.
- Activity resume does not count as Library-ready. A bounded UI probe requires a visible selected English Library tab, a draw and window focus. Start-to-Library-ready is recorded only when those conditions are met. Otherwise it remains NOT CONFIRMED. This detects rendered navigation, not loaded account data, a network-complete Library, playback or localized labels. No new loading screen is added.

## Checks and limits

Classloader fixtures pass own-class ownership, no parent miss for an owned default name, cached repeated lookup, missing-name fallback, packaged platform route and dependency parent-first routing. Timing fixtures pass last-sample refresh and placement, active-state acknowledgement safety, Library-ready elapsed persistence and marker cleanup. UI predicate fixtures pass visible/selected label detection. Android compilation and D8 pass. ART host load/reflection is 1,146 total, 1,145 loaded/reflected, with the same unused NativeActivity baseline miss. These are not Android UI/device tests.

The APK preserves the original private signer, package and label. Only classes15.dex and regenerated signatures differ from ZDfc80OT. v2/v3 signatures, clean CRC, stored resources.arsc and 16KB ZIP alignment were checked. No real account, playback, successful phone launch or actual-pixel check of the new report/probe is claimed. The report widgets themselves remain the prior bounded non-selectable dialog.
