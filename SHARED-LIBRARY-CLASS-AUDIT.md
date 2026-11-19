# Shared-library routing and class-reference audit

Candidate: https://gofile.io/d/pwoHvhfX

SHA256 `eab95ea30ee457455a53baa0a00e8f19956817e6f023ea8df24e3baeeec9ad7d`

Diagnostic marker: `music-shared-libraries-20261119`.

## Actual missing dependency

awuh is present in classes2.dex of the exact prior embedded plugin. It is also present in the donor, the transformed JAR and the bound JAR. It implements org.apache.http.protocol.HttpRequestHandler. The previous failure therefore did not justify copying another awuh definition into the APK.

The host and donor manifests already declare org.apache.http.legacy as an optional shared library. Android attaches this library to the app classloader. Shadow's specialClassLoader is the host loader's parent. For packages outside its host whitelist, Shadow bypasses the app loader and tries that parent followed by the plugin. Its built-in org.apache.http exception only applies below Android 9. Consequently Apache's interface was inaccessible on newer Android even though its library was declared. A prior failed resolution can later surface as awuh itself being unavailable.

## Change

The Music LoadParameters whitelist now includes these exact shared-library package families and their descendants:

- org.apache.http
- androidx.window.extensions
- androidx.window.sidecar
- android.adservices

The last three address the same visibility gap for the other optional shared libraries already declared by both manifests. The existing paradox.music.nativeui bridge entry stays unchanged. No blanket host fallback, unrelated package sharing, duplicate donor classes or hidden API access was added.

The APK changes only classes15.dex and signatures relative to the packed-resource candidate. The host JAR changes only NativeMusicActivity.class and NativeMusicDiagnostics.class. Embedded Music is byte-identical, including its packed-resource correction. Host manifest, resources and native libraries are unchanged.

## Regression test

The test uses the exact existing compiled PluginClassLoader and the actual transformed awuh.class, rather than a rewritten simulation of the routing code. A JVM BaseDexClassLoader adapter substitutes URLClassLoader, and android.jar represents boot/framework classes. Apache's SDK optional JAR is on the simulated app classpath, not the boot-parent classpath. Window and adservices fixtures test visibility only, not their real vendor implementations.

Before: loading awuh fails with NoClassDefFoundError for org/apache/http/protocol/HttpRequestHandler.

After, with the whitelist extracted directly from the production NativeMusicActivity source: awuh loads and its constructors/methods reflect; awuh remains plugin-owned and HttpRequestHandler is app-owned. Root and nested shared-library packages route correctly. java.lang.String still resolves through the boot path. An unrelated host-only fixture remains inaccessible.

This is a classloader regression test, not Android startup or rendering proof. The test sources are under tests/shared-libraries. Supply the current host JAR, transformed donor JAR, android.jar and Apache optional JAR separately. Adapt local paths as described in REBUILD.md.

## All-class reference audit

InventoryTypes scans every type_id across every plugin DEX and all definitions, including type references that appear only in annotations. The plugin defines 62,946 classes, as did the donor. The sole absent donor descriptor is android.app.AppComponentFactory, deliberately supplied by Android and replaced by generated plugin metadata in the transformed inventory. No donor implementation was dropped.

PluginReferenceGraph builds per-class edges from superclasses, interfaces, fields, method prototypes, instruction references and catch types. The starting set contains all 131 classes declaring generatedComponent plus all 12 custom view/fragment types in watch_while_layout and its recursively included mpp_player_bottom_sheet and watch_engagement_panel_layout layouts. Every XML type is defined. The transitive graph reaches 58,786 plugin classes.

The complete plugin type inventory has 1,707 references without a plugin-local definition. Most are Android or Java framework classes; Shadow/runtime bridge types come from the host. No newly missing donor implementation appears compared with the original APK. The new java.nio.LongBuffer reference is a framework class. The other new references are explicit Shadow and native bridge classes in the host.

Remaining donor-baseline external references include optional Window vendor interfaces, optional TLS provider implementations, annotations, newer/framework-internal Android APIs, and bundled cross-product extension code. OpenJSSE's support probe, for example, explicitly catches ClassNotFoundException. A bundled FileHelpers update-URI helper references an absent AndroidX FileProvider; no external call to that getFileUri helper was found in the decoded donor. YouTube-only and Reddit-only extension code also retains names not defined in Music. These pre-existing optional or unrelated paths are not repaired with arbitrary dummy classes, and the audit does not assert they will all execute successfully.

## Packaging and runtime limits

Same package, exact YouTube Ultra label and private signer. APK v2/v3 signatures, CRC, stored resources.arsc and 16KB ZIP alignment verified.

Not phone-tested. No visual UI result was available to inspect. Library rendering, authentication/account switching, notification/media-session behavior and playback remain unverified. This build fixes the reproduced dependency-routing defect and the same declared-library visibility gap; it does not promise to predict every future runtime error.
