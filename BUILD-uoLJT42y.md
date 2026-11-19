# uoLJT42y recovery build

Delta over CFH18AoO. APK SHA256: 8ccd3edce93ec7c81186e26c61c30fca668b53d4160117731570421faec31349.

The preceding phone build crashed on first launch, then did not open on second launch. The first-launch exception has not been captured, so this is a diagnostic/recovery build, not a validated crash fix.

- Cold-restored Music activity checks whether its plugin Application is ready before calling Shadow container startup. If not ready, it initializes diagnostics, bypasses the source-confirmed System.exit path, finishes the stale activity and returns to the host launcher. No data/cache is deleted.
- The existing host-startup diagnostic hook now captures uncaught exceptions even before Music is tapped. The prior crash handler is still called.
- Primary crash report is written independently of auxiliary trace files. Next launch reads a bounded summary off-main. Reports are retained until Copy/Close acknowledgement. Cold recovery notes never overwrite an existing exception report.
- Music payload, external-service unbind deadlock correction, ChatGPT hardening and earlier integration fixes remain unchanged. Only classes15.dex differs from CFH18AoO.

Verified: Android compilation; two-process early crash/recovery/Copy fixture; previous diagnostic fixtures; ART host 1,136 total and 1,135 load/reflect with the same unused NativeActivity missing-class baseline; v2/v3 signatures with unchanged signer; ZIP CRC; stored resources.arsc and 16 KB alignment; upload size and MD5.

These checks do not prove physical-device installation, startup, selected-account Library or playback. The exact cause of the reported first and second phone failures remains unverified pending the actual exception/report.

Later evidence: the subsequent complete ANR report identified selectable diagnostic-text rendering as the first-launch ANR, while GoogleApiHandler was idle. See STARTUP-SWEEP.md. The recovery APK still carried the older oversized dialog.
