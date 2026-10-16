<div align="center">
  <img src="./docs/outpost-icon.svg" alt="Outpost icon" width="160" height="160" />
  <h1>Outpost</h1>
  <p>A configurable, white-label content hub built with Kotlin Multiplatform and Compose Multiplatform.</p>
</div>

---

Outpost is a shared foundation for branded content apps. Its reusable core provides the shared UI and content features, while each app supplies its own branding and content configuration. The included Kotlin Outpost app is an example targeting Android, iOS, and Desktop (JVM), with articles, videos, and podcasts.

## Platforms

- **Android**
- **iOS** (device and simulator)
- **Desktop** (JVM)

## Project structure

- [`outpost-core/`](./outpost-core/) — reusable Kotlin Multiplatform code, shared UI, configuration, and content features.
- [`kotlin-outpost/shared/`](./kotlin-outpost/shared/) — the Kotlin Outpost app configuration and bridge used by the platform apps. Its `KotlinOutpostApp.kt` configures the app's theme, metadata, and content sources.
- [`kotlin-outpost/android/`](./kotlin-outpost/android/) — Android application.
- [`kotlin-outpost/desktop/`](./kotlin-outpost/desktop/) — Desktop application.
- [`kotlin-outpost/ios/`](./kotlin-outpost/ios/) — Xcode project and Swift app entry point for iOS.

## Get started

Install JDK 21. To run a platform app, use the IDE run configuration or the platform-specific instructions below. Android development also requires the Android SDK; running the iOS app requires macOS and Xcode.

### Android

Open the project in Android Studio and run the Android app configuration. To build a debug APK from the command line:

```bash
./gradlew :kotlin-outpost:android:assembleDebug
```

### Desktop

Run the desktop app with Gradle:

```bash
./gradlew :kotlin-outpost:desktop:run
```

### iOS

Open [`Kotlin Outpost.xcodeproj`](./kotlin-outpost/ios/Kotlin%20Outpost.xcodeproj) in Xcode, select a simulator or device, and run the app.

## Tests

Run the shared core and app bridge JVM tests from the repository root:

```bash
./gradlew :outpost-core:jvmTest :kotlin-outpost:shared:jvmTest
```

## Security

For responsible disclosure, see the [Security Policy](./SECURITY.md).

## License

Outpost and the apps built with it are licensed under the [GNU General Public License v3.0](./LICENSE).
