# Source hardening patch — 2026-11-19

This source-level hardening pass is not a verified fix for the device-specific Music Application startup ANR and does not establish working login, Library navigation or playback.

## Changes

- Removes the loader monitor from the wait surrounding the main-thread plugin Application lifecycle callback, so arbitrary plugin startup callbacks run without the extraction/loader monitor held.
- Makes extraction preload retryable after a failed extraction.
- Clears and recreates the native library cache when the extraction marker is missing or stale, avoiding reuse of an incomplete library directory.
- Applies readable/executable permissions to extracted native shared libraries.
- Uses the long package version code on Android 9+ for the host cache stamp.
- Handles null preload Context before installing diagnostics or scheduling work.

## Verification limits

The repository does not include donor APKs, the Android SDK, pinned build dependencies, build-time cache layout or a private signing key. This source has not been compiled against the pinned Android/Shadow dependencies here and has not been exercised on a physical device. Android Application lifecycle callbacks must remain on the main thread; moving the embedded Application.onCreate() wholesale to a worker thread is not a safe ANR workaround.

Before release, rebuild and validate startup, selected-account handoff, Music activity rendering, Library navigation, playback controls, media session, background playback and process restarts on physical devices.
