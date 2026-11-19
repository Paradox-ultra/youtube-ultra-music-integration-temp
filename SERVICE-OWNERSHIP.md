# Failed-bind service cleanup, 2026-11-19

Candidate: https://gofile.io/d/4tPo8XDE

APK SHA256: `8ff64f6c4402ecbd17eb9c63ecb8e51dfb04a167e7fe9c437bc0a8433c2c8bb0`

## Missed edge

The previous patch recorded framework connections only after a successful `bindService` result. The embedded GMS supervisor also performs unbind after a false bind result. Exact donor `sqk.e` enters its connection-map monitor and calls `sqi.d`. On failed bind, `sqi.d` calls `ssh.b`, which calls Context.unbindService, before that outer monitor is released. That connection was absent from the successful-bind tracker. It therefore fell through to Shadow's plugin unbind probe, which synchronously waited for main.

The donor main-thread listener migration needs the same connection-map monitor. Thus a failed external bind could still form the original cycle. The first captured phone worker stack followed this specific failed-bind cleanup branch. The latest report had only main, so its owner was not established.

## Change

`PatchServiceOwnership.java` modifies the loader only. Immediately before the ComponentManager's confirmed plugin bind dispatch it records the connection by object identity. False or throwing plugin binds remove that ownership. ShadowContext only dispatches unbind to the plugin manager for a positively identified plugin connection. Unknown and external connections, including failed-bind cleanup and framework-overload bindings, go directly to the framework. Plugin unbind removes ownership in a finally block. Connection identity is process-wide rather than keyed to a wrapper Context.

The old successful-external-bind hooks remain harmless compatibility no-ops. Framework exceptions are not hidden. Donor Application.onCreate, handler migration, locks, and lifecycle thread placement are unchanged.

## Path review

- ShadowContext explicit bind reaches ComponentManager. Its plugin-component predicate only reads registration maps, without a main-thread wait. External bind falls back to Android; implicit bind goes directly to Android.
- ComponentManager start/stop similarly guard plugin dispatch with the plugin-component predicate. Only real plugin service work enters the lifecycle dispatcher.
- The five PluginServiceManager methods still dispatch real plugin lifecycle work to main, with failure propagation and timeout from the earlier sweep. Merely replacing a latch with Future.get was never sufficient to remove a lock cycle.
- The previous unbind method unconditionally probed the plugin manager, even for unknown external connections. This was the missed main-thread wait. Positive ownership removes it.
- Donor supervisor service callbacks and timeout paths also synchronize on the connection map. Removing that synchronization or moving all Application startup off-main is not part of this change.
- Framework Binder/provider calls may still block independently. No claim is made that every possible platform or network stall is eliminated.

## Diagnostics and checks

During heartbeat stalls, a targeted multi-thread sample is refreshed every two seconds. GoogleApiHandler is first, followed by service/Binder-related stacks, blocked threads and latch/Future waiters. Idle blocking-queue workers are excluded. The normal main sample still refreshes every 500ms. Samples are bounded and captured off main; Java stack samples cannot identify monitor owners.

The actual Javassist patch is tested against synthetic classes for unknown/external cleanup, plugin dispatch, failed/throwing plugin bind cleanup and cross-context connection ownership. Trace tests check thread selection, ordering, last-sample refresh, active acknowledgement safety and persistent readiness timing.

Packaging checks: unchanged package, exact label and signer, v2/v3 signature verification, ZIP CRC, stored resources.arsc and 16KB ZIP alignment. Only classes15.dex and signatures differ from the prior candidate. Not phone-tested. No claim of measured startup improvement, successful authentication, Library data or playback.
