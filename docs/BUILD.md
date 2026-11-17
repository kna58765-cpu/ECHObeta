# Echo Android build

A push to `main` starts the Android APK workflow. The workflow installs Android platform 35 and build tools 35.0.0, runs `gradle assembleDebug`, and uploads `app-debug.apk` as the `Echo-debug-apk` artifact.
