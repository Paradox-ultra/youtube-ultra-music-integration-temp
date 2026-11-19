# Packed resource arrays and startup sweep

Candidate: https://gofile.io/d/hwuRbsh9

SHA256 `576f7f74f553cbdc5be4f862ea460555990ede77c6b4f3dbb8aba7388b79b84b`

## Theme failure

The donor manifest selects Theme.YouTubeMusic.Home for MusicActivity. The embedded metadata and resource table agree on ID 0x801506bc. Its parent chain is Home -> NoActionBar -> Theme.YouTubeMusic -> Base.Theme.YouTubeMusic. The host MusicTaskActivity and MusicProxyActivity declarations have no explicit theme override. Shadow's normal activity initialization applies the donor ActivityInfo theme, falling back to the donor Application theme for activities with no override.

The actual mismatch was in AppCompat's styleable array. During conversion, dex2jar encoded large int arrays as hexadecimal strings decoded at class initialization. The scalar resource rebase did not touch those strings. le.j therefore requested original 0x7f attributes while the Music resource table uses 0x80. In particular, index 117 requested 0x7f0408d0 rather than Music's actual windowActionBar 0x800408d0. A correct Music theme then failed AppCompat's hasValue check.

## Exact correction

RebasePackedResourceArrays repairs only hexadecimal int-array strings immediately passed to dex2jar's decode_I helper. Each little-endian int is changed only if it belongs to the exact original donor public resource-ID set. Framework attributes and unrelated numbers remain unchanged.

The full embedded APK scan found two affected strings: le's AppCompat array and com.google.cardboard.sdk.R$styleable's equivalent array. Each contains 125 donor resource IDs and two framework IDs. All 250 donor IDs were rebased. No original donor scalar resource-ID literals or native DEX int-array payload entries remained. A second pass found no remaining changes.

VerifyPackedArrayDelta compares instruction semantics independently of DEX string/index reordering. It checked 62,946 classes and 238,613 methods. Only two const-string instructions changed, both in the identified styleable class initializers. The embedded APK otherwise preserves its resources, manifest, assets, native libraries, class inventory and method inventory. Only embedded classes.dex and classes4.dex change.

No AppCompat exception is skipped. No global host theme replaces the donor's individual activity themes.

## Startup path review

REA decompiled the exact transformed MusicActivity.onCreate from a scoped APK. Static review followed layout inflation and setContentView, fragment-container lookup and saved-state navigation, subsequent view/resource IDs, controller setup, activity lifecycle registrations, navigation bar/theme use and playback/UI-controller initialization. Decompiler warnings and missing dependency bodies mean this is a review, not runtime proof.

Reviewed existing integration boundaries:

- Hilt activity attach reads the injected container's plugin Application. Service attach receives its owning plugin Application wrapper. Providers attach to Music Application, and all 85 receiver callbacks already resolve their classloader-owned Application.
- Plugin resource selection, classloader and inflater remain scoped to Music. Scalar resource IDs are already rebased. The packed-array correction addresses the newly confirmed gap without arbitrary theme or fragment changes.
- The account bridge seeds the donor preference store before its identity-controller constructor reads it, copies only Ultra's selected identity keys and never selects the first device account. A bound controller is synchronized and verified on the worker before launch. This does not prove token exchange or server acceptance.
- External and failed-bind service cleanup bypasses plugin main-thread waits; real plugin lifecycle work remains on main. Notification/media-session behavior still needs Android execution.
- The existing WebView initialization dispatcher and plugin inflater remain intact.
- Rechecked 29 host and 20 Music ARM64 ELF libraries: no unresolved declared native dependencies against bundled/platform libraries; minimum load alignment 16KB. This does not prove successful ExoPlayer/native runtime behavior.

No additional source-confirmed failure justified speculative rewrites of donor fragments, network loading, authentication or player code. Static review cannot promise every future error is fixed.

## Packaging and limitations

Only host classes15.dex (diagnostic marker) and the embedded plugin asset differ, apart from signatures. Host manifest, resources and libraries remain byte-identical. Same package, exact YouTube Ultra label and signer. APK v2/v3 signatures, ZIP CRC, stored resources.arsc and 16KB ZIP alignment passed.

Not phone-tested. No measured rendering, Library data, selected-account authentication, notification/media-session or playback success is claimed. No visual UI result was available to inspect.
