# Outpost

Outpost is a Kotlin Multiplatform project for building a white-label content hub across Android, iOS, and desktop. The repository is split into a reusable shared module and a concrete app implementation that consumes it.

The core shared module is `:outpost`. It contains the Compose Multiplatform UI, shared resources, and platform-aware Kotlin source sets. The main shared UI entrypoint starts in `outpost/src/commonMain/kotlin/dev/appoutlet/outpost/App.kt`.

The repository also includes `:kotlin-outpost:*`, which is the first app built on top of the shared Outpost module. That app provides the platform launchers and a thin bridge layer that connects Android, iOS, and desktop entrypoints to the shared UI.

At a high level, the project is organized like this:

- `outpost/`: reusable shared product code and UI
- `kotlin-outpost/shared/`: bridge module that exposes the shared app to platform targets
- `kotlin-outpost/android/`: Android app entrypoint
- `kotlin-outpost/desktop/`: Desktop JVM app entrypoint
- `kotlin-outpost/ios/`: iOS Xcode project

Outpost uses Compose Multiplatform for UI and Kotlin Multiplatform source sets to keep most application code shared while still allowing target-specific code where needed. Automated tests currently live in the `:outpost` module, with focused tasks for JVM, Android host, and iOS simulator targets.
