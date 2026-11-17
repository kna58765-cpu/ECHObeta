# Echo

Echo is an Android internet messaging app with a dark, modern technology-inspired interface.

## Current milestone

This repository starts the Android application foundation and a polished chat UI prototype. The first build is designed to run without private service credentials. Live internet messaging requires connecting a Firebase project in the next integration milestone.

## Build

GitHub Actions builds a debug APK on pushes to `main` and manual workflow dispatch.

## Planned features

- Account sign-in and registration
- User discovery and friend requests
- One-to-one real-time text messaging
- Message history and delivery timestamps
- Later: image sharing, groups, and voice features

## Tech stack

- Kotlin
- Jetpack Compose
- Material 3
- Firebase Authentication and Cloud Firestore (backend integration milestone)

Package: `com.echo.chat`
