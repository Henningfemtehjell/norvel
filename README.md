# Norvel Android

Android-wrapper for https://norvel.no/app/

- App ID: `no.norvel.app.debug` for this test build
- Min Android: 7.0 (API 24)
- Target: Android 16 (API 36)
- Web content stays on `https://norvel.no/app/`

The GitHub Actions workflow builds an installable test APK named `norvel.apk`.

## Important
The first APK built here uses Android's debug signing key on the GitHub runner. It is intended for testing. Before public distribution and long-term updates, configure a permanent private release signing key in GitHub Secrets and build a release APK/AAB.
