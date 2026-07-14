This is a Kotlin Multiplatform project targeting Android, iOS, Desktop (JVM).

* [/kotlin-outpost/ios](./kotlin-outpost/ios/ios) contains an iOS application. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/outpost-core](./outpost-core/src) contains the root Kotlin Multiplatform module for shared code and UI.

* [/kotlin-outpost/shared](./kotlin-outpost/shared/src) contains the KotlinOutpost bridge module that fronts the platform apps.

* [/outpost-core](./outpost-core/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./outpost-core/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./outpost-core/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./outpost-core/src/jvmMain/kotlin)
    folder is the appropriate location.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :kotlin-outpost:android:assembleDebug`
- Desktop app:
  - Hot reload: `./gradlew :kotlin-outpost:desktop:hotRun --auto`
  - Standard run: `./gradlew :kotlin-outpost:desktop:run`
- iOS app: open the [/kotlin-outpost/ios](./kotlin-outpost/ios) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :outpost-core:testAndroidHostTest`
- Desktop tests: `./gradlew :outpost-core:jvmTest`
- iOS tests: `./gradlew :outpost-core:iosSimulatorArm64Test`

### Static analysis

This project uses [Detekt](https://detekt.dev/) for Kotlin static analysis. The Detekt plugin is applied to every module and runs with auto-correction and the formatting rule set enabled.

Run Detekt from the repository root:

```bash
./gradlew detekt
```

### Security

If you discover a security vulnerability in Outpost or in any app built on top of Outpost, please review our [Security Policy](./SECURITY.md) for responsible disclosure instructions.

### License

Outpost and all the apps made with it are licensed under the [GNU General Public License v3.0](./LICENSE).

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
