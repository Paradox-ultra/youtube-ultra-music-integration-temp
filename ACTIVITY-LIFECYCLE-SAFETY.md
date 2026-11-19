# Activity lifecycle safety, 2026-11-19

Candidate: https://gofile.io/d/Sp1KUXs2

SHA256: `0dc0af16e0cf1a6bd1bd11826ab8996371f7edc1e7803e0800bc470fd45bc64a`

## Observed failure and limit

The visible null activity callback came from MusicProxyActivity's recovery call to framework superOnCreate. The original delegate initialization had already thrown. Its caught report was overwritten by the later uncaught fallback exception. Normal Shadow startup publishes its activity after context/theme initialization and before donor onCreate, so the observed stack does not establish an ordering bug during successful startup. The original primary exception is unknown.

## Correction

- ShadowActivityDelegate.getPluginActivity now returns its actual nullable backing field without a Kotlin non-null assertion. All lifecycle wrappers share this observation path. Their existing owner check ignores an absent/unowned activity. The existing PluginActivity.get fallback placeholder remains unowned and is not delivered to callbacks.
- Failed initialization detaches both the PluginContainerActivity delegate field and its GeneratedPluginContainerActivity superclass field. Clearing only one would leave some later generated lifecycle methods dispatching to a partial delegate.
- MusicProxyActivity calls the required framework superOnCreate at most once, after detachment on failure. Any recovery exception is suppressed beneath the original initialization failure. It then returns to the host with the original failure saved and the existing bounded report available.
- Four stage markers identify dependency injection, activity construction, context/theme initialization and donor onCreate. No early publication, asynchronous lifecycle workaround or donor callback reordering was added.

The failed/cold activity path is also detached before framework recovery. Later start, resume, pause, stop, save, restore, destroy and generated delegate methods fall back to the framework rather than using an absent plugin.

## Checks

The exact Wrapper source was compiled against synthetic platform/delegate stubs. The actual bytecode patch was applied to the fixture. All 21 Android lifecycle callback phases were exercised before plugin publication, for a live owned plugin, and after both-field detachment. Absent/dead activities delivered no plugin callbacks; live owned callbacks were preserved. The early onActivityPreCreated wrapper remains intentionally empty, as upstream routes it through its separate plugin pre-created notification.

The actual production getter/field names and four stage call counts are asserted by the patch. Compilation and D8 succeeded. APK package, label and signer remain unchanged; v2/v3 signature verification, ZIP CRC, stored resources.arsc and 16KB ZIP alignment passed. Only classes15.dex and signatures changed from the prior candidate.

Not phone-tested. This fixes the secondary lifecycle/recovery crash and retains the first failure. It does not establish that the unknown primary initialization failure, Library rendering, authentication or playback is fixed. No new visual layout was added; the existing bounded report remains.
