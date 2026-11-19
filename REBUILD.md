# Rebuild notes

## Inputs and exclusions

Supply the original host and Music APKs referenced in UPSTREAM.md separately. They are not committed. For incremental reproduction of the latest patch layer, supply the matching prior integration APK and embedded Music payload locally. The private original integration key is not distributed. Signing with another key changes the install/update identity.

The final delivered build has package app.paradox.youtubeintegrated, label YouTube Ultra, a native Music entry replacing the compass route, and an isolated embedded Music payload. Music's own j$ classes stay plugin-owned; host Kotlin for Shadow is shaded.

## Sequence used

1. Convert and transform donor classes using the pinned Shadow transform modules. Rebase resource IDs to 0x80, bind the selected identity hooks, and generate PluginManifest metadata. Preserve original j$ smali rather than dex2jar's reconstructed j$ bodies.
2. Restore every temporary underscore alias to the original Android DEX class name with RestoreDexNames.java and donor-name-restoration.tsv. Audit all class and j$ method references. The fixed embedded payload contains 62,946 classes and 582 original j$ classes.
3. Compile bdbl with PatchSslListener.java, D8 that one class, remove its old class definition with ReplaceDexClass.java, and append the replacement as a new DEX. This avoids exceeding the original DEX method-ID limit. Never retain duplicate bdbl definitions.
4. Compile all native-loader/src Java against the Shadow runtime and android.jar. Assemble the core jars, shade Kotlin, apply PatchPackageInfoAdapter, PatchDiagnosticExits and PatchProviderManager in that order. For CFH18AoO, compile the included current diagnostics and NativeMusicServiceConnections helper, then apply PatchExternalServiceRouting to the final host jar before D8 at min API 26. This patches two successful framework-bind routes and the matching direct framework-unbind route without changing plugin service lifecycle. D8 the result at min API 26.
5. Replace only classes15.dex and, when needed, assets/ultra_music/music.apk in the prior signed integration APK. Drop old signature entries, keep resources.arsc uncompressed, zipalign with 16 KB native alignment, and sign with a locally supplied key. Never embed signing material in the repository.
6. Verify v2/v3 signatures, stored/aligned resources, manifest/name/package, changed ZIP entries, duplicate classes, DEX references and relevant JVM/ART fixtures.

Development path names in legacy scripts are not portable defaults. Compile.java invokes the JDK compiler when a separate javac binary is unavailable. Build tools and SDK/Shadow jars must be supplied in the documented layout or paths adapted.

## Verification limits

Synthetic fixtures and ART load/reflection do not prove Android application startup, JNI execution, UI rendering, account handoff or playback. The latest build passed earlier signature/reference/provider/guard failures but now has a startup ANR. Do not publish an APK from this repository as a working finished product without Android runtime verification.
