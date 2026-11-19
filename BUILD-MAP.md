# Delivered builds and recovery commits

These are reconstruction commits created after APK delivery, not original-time build commits. Exact APK hashes identify delivered binaries. They do not establish bit-for-bit reproducibility from these sources alone.

| Build ID | Change | Recovery commit | APK SHA256 |
|---|---|---|---|
| kcFMKeA6 | Firebase installed-certificate PackageInfo adapter | `840abf2fc7d105114d8c7a680d82d80387c3afb9` | `ea2da6d36f45c0163cbd006b09dae7a104a8bb339d398166351c1392415111af` |
| TvofcMyp | full caught-exception Copy dialog | `b2d0ad6e0a9d6cca357e4d57504414e7d82410ee` | `86b2e0cad2b80ff743c1886a2cb2ff83ab6cd5637c235b7c804fd61dcc069d07` |
| gSQzE9om | restore original DEX companion and class reference names | `e3894b55a652d4618072e7f448c1e0c26dfc0cb1` | `833598084acb2ad8065a9ad48139259867ebaf9a0f3ac6bdf599aea3897076b2` |
| f55vCyzv | idempotent provider startup and metadata ordering | `a520bf7bb5d443192e192e1c125e6796b162fc63` | `8eb24a55439f9828b4e56a8ddac1eb9805ecbbfa24733a4f8ea899c909849b86` |
| 3Y1CLQKd | reuse verified host process SslGuard, superseded strict-order variant | `3c75b12f52a108e31bd171f8b74a2df6a4f911b0` | `edb6b98d40d5b28869ad015e183d3bc59f39c05bde90dd664232a1dd10b34b6e` |
| BqoDF89d | reuse initialized host SslGuard at any installed position | `07fcf61df052bfaaaba8b523ad1df4ee2cf40038` | `ab391188413a2596db00e71d018094d157a3e7f180c4193dda61078d585ef5ae` |

## Later diagnostic and corrected builds

- z1S9S64v: diagnostic-only predecessor, SHA256 `f760ed2186dfb60b2528da3363893f8b865237aeec4e54cb580ca391642d7969`. The current source is a later evolution, not an exact source tree for that binary.
- CFH18AoO: ChatGPT source hardening plus targeted external-service unbind deadlock correction. Source commit `9326a1bddbe51af7fafc84e7179db8d1147a695b`. SHA256 `5b1d076595c68dd492e68125e69c73efc5b601f487dd1726388f994e83374b31`.

Latest delivered: CFH18AoO. See BUILD-CFH18AoO.md. Physical-device validation remains open; no successful full Library/login/playback claim.

## Recovery build uoLJT42y

Source `609fafe00243b1bf9a4f8144f40ac85e49119769`; SHA256 `8ccd3edce93ec7c81186e26c61c30fca668b53d4160117731570421faec31349`. Latest delivered diagnostic/recovery build, not a verified working release. See BUILD-uoLJT42y.md.
