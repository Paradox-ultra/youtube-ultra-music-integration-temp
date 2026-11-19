# Library network/auth observation

Candidate: https://gofile.io/d/KvJfoSxD

SHA256 `73b663951d54915a66dfe40a8ca00e43de6f451dd62a5611a4501e29494552f7`

Marker: `music-network-observation-20261119`.

## Current boundary

The preceding shared-library build reached native Music's Library. The displayed cloud/error state does not identify a single cause. This candidate adds local observation to obtain the missing failure evidence. It is not a claimed Library network/auth fix and was not phone-tested before delivery.

Static review found:

- Host manifest has INTERNET and ACCESS_NETWORK_STATE. Shadow delegates ConnectivityManager to its base host context rather than inventing a network state.
- Host and donor network-security configurations are identical. No TLS trust configuration was weakened.
- Host and Music Cronet native libraries have different versioned filenames, 133.0.6876.3 and 140.0.7259.0. This excludes a same-filename collision, not every JNI or native-runtime problem.
- Selected-account preference seeding and verification operate on the chosen Ultra identity. This is identity metadata, not proof that authentication succeeded. No host token store is scraped or copied.
- The donor token path is agyq.a -> rqi.a -> rol.b or the donor's alternate auth API. Host and donor use the same microG vendor and actual installed package/UID. The Android auth service resolves the installed package, whose existing client metadata is retained.
- Donor API client configuration, endpoints, cookies/visitor generation and all request construction remain unchanged. There is no demonstrated wrong API key or missing visitor value to repair speculatively.
- REA's exact transformed ahtk.a error mapper distinguishes HTTP 401/403/500, auth errors and IO/network errors. Several paths become error/offline UI. Therefore a cloud icon is not proof of a failed connectivity check or proof of invalid authentication.

## Observation points

PatchNetworkObservation inserts calls directly into the exact existing DEX. It does not recompile donor Java or change return values, exceptions, headers, request bodies, identity selection or network policies.

The observer covers:

- All 20 concrete implemented onResponseStarted/onFailed methods with the Chromium callback signature, including wrappers and app callbacks.
- All three concrete Chromium-compatible request start implementations: native CronetUrlRequest, Java fallback cecr and Android HTTP bridge cdyp.
- Token request start and successful return through agyq.a, plus account enumeration returns through agyq.d/e. The latter includes feature-filtered queries. A negative match in one such query does not establish that the selected account is absent from the device.
- ahtk.a donor error mapping, ahbw.k connected-or-connecting results and Chromium connection-type results.
- LoadingFrameLayout's state change after its original code. A diagnostic is requested only if a currently shown frame remains in error state 4 for another 1.5 seconds. A weak reference and attempt-generation check prevent a stale view from triggering a new attempt's report. Only one network dialog is requested per Music-open attempt.

The existing local Copy/Export dialog receives the safe observation text. No custom loading screen is added. Reopening Music starts a fresh observation attempt. A transient frame that becomes successful before the delay should not show a network report. This behavior has not been observed on Android.

## What is recorded

Only fixed endpoint categories, HTTP/numeric Cronet status codes, exception class names, header-name presence booleans, account/identity-match booleans, token-presence boolean, system network capability flags and timing/counters.

URLs are parsed transiently and reduced to an allowlisted host and fixed endpoint name, such as browse. Raw URLs, queries, arbitrary paths, bodies, header values, account names, account IDs, tokens and exception messages are never put in the network report. Header entry values are never read. TokenData is inspected only to determine whether a token was returned; no credential is retained by the observer.

A 24-entry ring bounds event storage. Last browse request/response, last auth result and last account-query result are also retained separately so that image or logging requests cannot erase those facts. Response/failure counters count callback events, not distinct requests; wrappers may observe one response more than once.

The system snapshot reports permissions, active-network/capability presence, internet/validated/cellular/Wi-Fi/VPN flags, metering, background restrictions and whether Ssl_Guard is installed. It does not independently test remote reachability. Plugin-level connectivity results are observed without overriding them. A value of -1 means not observed.

## Checks

- Full semantic comparison passed for 62,946 classes and 238,613 methods. After removing the 33 inserted observer calls, original operations, register use, references, branch/switch destinations and catch regions match. Changes affect 30 methods across 18 classes. Class/method inventory is unchanged.
- All 33 plugin invocations resolve to the 10 expected public static methods in the final host DEX.
- The only newly external plugin type is the host's NativeMusicNetworkDiagnostics bridge, within the existing host-package whitelist.
- The privacy test passed against the final host JAR. It supplies synthetic credentials/identities in URLs, headers, token data and exception messages. Header entries throw if their values are read. Reports omit all sentinels, identities, raw URLs/paths and exception messages. Browse status/header-presence facts survive more than 24 later events.
- The final APK retains package, label, manifest, resources, native libraries and signer. Only embedded Music and classes15.dex change, apart from signatures. v2/v3 signatures, ZIP CRC, stored resources.arsc and 16KB ZIP alignment pass.

Tests and patch/verifier source are included. Supply matching private APK/JAR inputs and SDK/build tools separately. No APK, key, private phone report or credential is in this repository.

## Remaining uncertainty

No observed failing request/status from the actual device is yet available. There is no basis to select an auth, TLS, connectivity or server-side fix. Missing observed browse activity could mean request gating, cache behavior, an earlier failure or incomplete observation coverage; it is not proof that the app made no network requests. Optional filtered-account queries and callback-event counts also need their stated context.

Not phone-tested. The new dialog's rendering and trigger timing have not been visually verified. Library loading, authentication/account switching, playback and notification/media-session behavior remain unverified. This build adds evidence, not a promise that all future errors have been fixed.
