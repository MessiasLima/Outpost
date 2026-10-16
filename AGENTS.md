
# AGENTS.md

## Repo Shape
- Outpost is a Kotlin Multiplatform white-label content hub. `:outpost-core` is the reusable shared module; `:kotlin-outpost:*` is the first concrete app built on top of it.
- Shared UI starts in `outpost-core/src/commonMain/kotlin/dev/appoutlet/outpost/App.kt`.
- `kotlin-outpost/shared/src/commonMain/kotlin/dev/appoutlet/outpost/kotlin/KotlinOutpostApp.kt` is the thin bridge that platform apps call.
- Platform entrypoints: Android `kotlin-outpost/android/src/main/kotlin/dev/appoutlet/outpost/kotlin/android/MainActivity.kt`, Desktop `kotlin-outpost/desktop/src/main/kotlin/dev/appoutlet/outpost/kotlin/desktop/Main.kt`, iOS `kotlin-outpost/shared/src/iosMain/kotlin/dev/appoutlet/outpost/kotlin/MainViewController.kt` with the Xcode project in `kotlin-outpost/ios/Kotlin Outpost.xcodeproj`.

## Commands
- Use only focused test tasks needed for the files or target you changed. When verifying a specific change, run the narrowest test task or class possible (e.g. `./gradlew :outpost-core:jvmTest --tests "dev.appoutlet.outpost.feature.home.HomeViewDataMapperTest"`) instead of the full module or project test suite.
- NEVER run `:outpost-core:allTests`, `build`, or `assemble` unless the user explicitly asks for them.

## Quirks
- `detekt` has `autoCorrect = true` in every Gradle module, so lint can rewrite files.
- `detekt` runs automatically before every `git push` (`config/githooks/pre-push.sh`) and blocks the push on failure, so avoid running it manually unless explicitly demanded.
- To check an implementation or basic compilation, prefer the smallest relevant test task or small set of test tasks instead of broad project verification.
- Compose resources live under `outpost-core/src/commonMain/composeResources`; the `dev.appoutlet.outpost.generated.resources` API used from Kotlin is generated.
