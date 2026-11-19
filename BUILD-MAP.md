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

Latest delivered: BqoDF89d. Application startup ANR remains open. No successful full Library/login/playback claim.
