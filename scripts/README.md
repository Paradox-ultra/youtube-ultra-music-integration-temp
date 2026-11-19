# Build scripts

The preserved legacy scripts use a development-machine layout rooted under /tmp and /downloads. They are recovery records, not portable build entry points. They depend on donor APKs and Android toolchains which are not stored in this repository.

Use REBUILD.md as the authoritative manual sequence. Do not run scripts against untrusted APKs or use a private signing key in CI.

The source-level hardening changes and verification limits are documented in SOURCE-HARDENING-2026-11-19.md.
