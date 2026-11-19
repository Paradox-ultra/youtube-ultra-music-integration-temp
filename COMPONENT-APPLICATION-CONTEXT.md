# Plugin component Application context

Candidate: https://gofile.io/d/hw7HHz9a

SHA256 `09f67ca0848af09ccbf4e0875287714ca6235f4a6469bc875b1cc109b9894ed8`

## Actual failure

MusicActivity.attachBaseContext calls bdns.a on its incoming base argument before calling super. bdns calls bcxi, which reads that argument's applicationContext and expects the plugin's GeneratedComponentManager. Shadow passes the host container Activity as the base. ShadowContext.getApplicationContext already returns the Music Application correctly, but that override is not involved when the incoming raw host base is inspected first.

## Correction and scope

PatchComponentApplicationContext adds an accessor on ShadowActivityDelegate that returns its injected plugin Application only after dependency injection is complete. PluginContainerActivity.getApplicationContext uses that value when present, falling back to the framework otherwise. This retains the base object's Activity identity, unlike wrapping an Activity in a plain ContextWrapper. It does not replace Ultra's global Application, publish the plugin Activity early, weaken Hilt's check or share unrelated host component classes.

ShadowService.setHostContextAsBase wraps its incoming host base with NativeMusicComponentContext.serviceBase. The wrapper exposes the service's existing owning Music Application to donor attachBaseContext while delegating base facilities. It avoids a redundant wrapper if the base already exposes that Application. Missing plugin Application fails before attach rather than silently selecting the host.

## Other components checked

- ShadowContext returns mShadowApplication. The delegate injects it before component attach.
- ShadowApplication returns itself as applicationContext.
- Provider creation is called with the plugin Application and attaches each provider directly to that context. No change was needed.
- Receiver transformation already resolves PluginPartInfo by the receiver classloader and calls the donor callback with info.application. Inspection of the exact embedded APK found 85 onReceive wrappers, zero unwrapped receiver methods and 14 bcxi entry-point call sites. The audit counts do not prove receiver runtime behavior on Android.
- The same bdns attach-base pattern occurs in other Music activities. The common container correction covers those without per-activity donor edits.

## Verification

The actual bytecode patch was applied to a synthetic fixture. It checks pre-injection framework fallback, injected Application identity before attach, preserved Activity identity, service attach context, delegated base facilities, redundant-wrapper avoidance and an unchanged host Application.

Compilation and D8 passed. The package, exact label, embedded Music APK and signer remain unchanged. Only classes15.dex and signatures differ from Sp1KUXs2. APK v2/v3 signatures, ZIP CRC, stored resources.arsc and 16KB ZIP alignment passed. Deadlock and failed-lifecycle safety changes are retained.

Not phone-tested. This corrects the reported base-context identity leak. It does not establish successful Library rendering, login, selected-account data or playback. No visual layout was changed.
