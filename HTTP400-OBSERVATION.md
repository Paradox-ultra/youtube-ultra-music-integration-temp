# HTTP 400 observation build

This is a diagnostic, not a confirmed Library repair. It adds no guessed headers or configuration values and does not change authentication, request bytes, retries, or HTTP response handling.

Build marker: `music-http400-observation-20261119`.

APK SHA256: `3fbca3954ee6462795e148d7a974b42f64db0aa52635e4e39694893a7be80cc2`.

## Source trace

The native InnerTube request is `anau`, not the generic `asqu` path. Its `ab` method sets `Content-Type: application/x-protobuf` and `X-GOOG-API-FORMAT-VERSION: 2`. `al` serializes its native request and optionally gzips it. `amyq.a` sets ClientInfo client enum and version. The version is obtained through `amyn` and `aibu`, with preference/ReVanced overrides. The package-info adapter retains donor metadata.

The donor's ClientInfo schema maps client enum to protobuf field 16 and version to field 17. The native context's ClientInfo is field 1. Browse request context is field 1. The observer reads these from the serialized bytes that are actually passed to the network builder. It does not replace the donor's protobuf library or alter serialization.

Absence of X-YouTube-Client-Name/Version headers alone is not a defect in this native protobuf path. Visitor headers are conditional. No missing-header repair is claimed.

## Added observations

- `ahjt.e`: already-materialized outgoing body, selected safe header classifications, client/version allowlist, gzip declaration/magic/decompression validity, visitor header/override presence, and an optional in-body visitor-byte presence comparison.
- `ahjt.f`: associates those observations with the returned Cronet request on the same thread.
- `agyq.a`: classifies service/scope selection as booleans. The donor base set contains YouTube and YouTube force-SSL; late impersonation is an allowed additional scope. Unknown scope strings are never printed. A base-set match is an observation, not proof that a token is accepted by the server.
- `ahmk` byte-array constructor: classifies buffered HTTP400 bodies without changing the array.
- `ahmk` iterable constructor: inspects only materialized collections of ByteBuffers through read-only duplicates. Original positions and limits are unchanged. No response InputStream or network read is invoked.

Existing request-start and response observations now add browse payload/auth/status correlation. Response callbacks are deduplicated for this correlation. Original broad response counters still count callbacks.

## Bounds and privacy

Error buffers are capped at 64 KB for classification. Request bodies and gzip expansion are bounded at 2 MB. Only fixed JSON status/reason labels, fixed message categories, and RPC-status-compatible protobuf code labels can enter reports. Unknown values become a fixed unknown label. Raw messages, raw bodies, raw header values, credentials, account identifiers, URLs, browse IDs, query strings and payload hashes are not reported.

Authorization values are examined only transiently to choose a fixed scheme label. Visitor values are examined transiently for nonempty/presence comparisons. Neither is retained in the observation metadata.

Client output uses fixed client labels. Version output distinguishes the known donor version 8.30.54, another numeric-shaped version, missing, and invalid/unrecognized. It never prints an arbitrary version string.

Browse groups compare identical top-level serialized fields with the context field excluded. Their internal digest is never printed. At most 16 groups and 12 correlation rows are retained per attempt. Group 0 means unavailable. A matching group is not proof that the donor consciously chose a retry, and no extra unauthenticated request is generated.

A matching visitor-byte comparison is not schema validation. An unavailable comparison does not establish that visitor data is absent. Header/override presence is reported separately.

## Verification

- 62,946 classes, 238,613 methods.
- 38 observation calls in 34 methods across 20 classes, including the previous network observation hooks.
- Original operation/register/reference, branch/switch, and catch-region semantics match after stripping only observation calls.
- All 38 calls resolve to 15 public static host bridge methods.
- Runnable privacy fixtures cover synthetic credentials, unknown error fields, fixed RPC status classification, malformed/oversize gzip, context-independent browse correlation, auth/no-auth statuses, unchanged byte arrays, and unchanged direct ByteBuffer positions/limits.
- APK v2/v3 signature and 16 KB zip alignment verified with the existing signer.
- Outside APK signatures, only host `classes15.dex` and the embedded Music plugin differ from the previous diagnostic. Manifest, app label, resources, permissions and package are unchanged.

The final APK has not been validated on the user's device. No successful native Library load, account data retrieval, or playback is claimed.
