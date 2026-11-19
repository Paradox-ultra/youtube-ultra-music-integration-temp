# Combined startup sweep candidate

Candidate APK SHA256: `1851afb41cbf04aea27f8025cb4805fc3e1c873809a6d03eefae2def4ee861f3`.

This is not a claim that every possible defect is fixed. The phone's later ANR stack establishes selectable diagnostic TextView rendering as a separate problem. Its Google API handler is idle, so the earlier external-service unbind deadlock is absent in that snapshot. Raw reports are private and excluded.

## Changes over uoLJT42y

- Diagnostic visible text is non-selectable and limited to 12KB / 100 input lines, plus a short footer. A complete local report can be exported through the existing non-exported, URI-grant FileProvider. Report assembly and file IO remain off-main. Only an explicit share-chooser action exposes the report to a selected destination.
- Both Shadow stale-container exit sites finish without calling System.exit. The specific cold-restored Music activity guard remains.
- Music Application initialization uses one retained FutureTask per process. It runs on main without holding the extraction/loader lock. Success and failure are both terminal for that attempt, preventing duplicate Firebase/provider/Application initialization on retries.
- The five plugin-service dispatch entry points preserve execution on main, returning results and failures to waiting workers. A bounded FutureTask replaces the reachable inline unguarded latches, including cancellation before queued work runs when interrupted/timed out.
- WebView resource initialization uses the same main-thread dispatcher, avoiding the original indefinite latch if WebView throws an Error or if initialization is requested from main.
- Native library selection uses the actual process bitness. No matching ABI now produces a clear error instead of silently declaring an empty extraction successful. Cache validation checks the expected native file sizes/readability/executability before trusting a marker. Incomplete cache rebuilds touch only the dedicated generated code-cache directory, not user data.

`PatchStartupSweep.java` is applied to the merged host/runtime JAR after the prior recovery and external-service routing patches, before D8. Preserve the patched ShadowContext; do not replace it with upstream bytes.

## Checked

- Android compilation and D8 completed.
- Synthetic tests cover main/worker dispatch, primitive return, Exception/Error propagation, at-most-once Application initialization after both success and failure, process-bitness ABI selection, and a 50,000-line input bounded below 13KB/105 lines.
- ART host load/reflection: 1,142 classes, 1,141 loaded/reflected. The one unused NativeActivity missing-class case is the pre-existing baseline, not a new failure. This is not Android UI execution.
- 49 ARM64 ELF files across host and embedded Music have machine 183, complete packaged/platform DT_NEEDED dependencies, and minimum PT_LOAD alignment at least 16KB.
- Manifest provider authorities are unique. Existing provider export/permission policies are unchanged. No permission widening. Music components remain virtualized behind the existing container rather than blindly merging the donor manifest.
- APK CRC is clean; resources.arsc remains stored; 16KB ZIP alignment and the same private v2/v3 signer are retained. The production delta is classes15.dex plus regenerated signatures. No donor asset/native library or selected-account routing change.

## Runtime validation

Physical Android 16 ARM64 validation is pending. No real account login, selected Library, playback, phone installation or full second-process-launch success is claimed by these static/unit checks.

## Physical test attempts and delivery

The unchanged candidate was made available as ZDfc80OT, SHA256 as above. It is not phone-tested.

First physical Android 16 ARM64 Pixel 10 run: the instrumentation logged the device information, then Android killed the host for a package-change event during its clean-device intro before the host-launch checkpoint. The missing normal MicroG prerequisite was also logged, but is not established as the kill cause. No successful Music lifecycle claim follows.

Second run added the official normal MicroG prerequisite and used the isolated startup fixture in `tests/java/StartupInstrumentationIsolated.java.txt`, deferring the launcher. Test Lab retried automatically three times and terminated with infrastructure Internal System Error 3, gcloud exit 20. That is not a test pass or a diagnosed production failure. Further Test Lab submissions were stopped.

No real account login, selected Library, playback, full host startup/recreation or second-process launch is verified by these attempts. The crash reporter remains included. Raw device logs, results and transfers are excluded from this public source repository.
