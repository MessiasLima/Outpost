# Generated variant structure

`scripts/scaffold-variant.sh <variant-name> [options]` writes everything below from
`templates/manifest.txt` and patches `settings.gradle.kts`. This file documents what comes out,
which identifiers are derived, and what the scaffold deliberately leaves out.

Nothing here is tied to a particular brand: the variant name, the display name, the package and the
set of platforms are all parameters.

## Derived identifiers

Every value is computed from the variant name unless an option overrides it, so any name that
follows `*-outpost` reproduces the shape of `kotlin-outpost`:

| Derived value | Rule | `rust-outpost` | Reference (`kotlin-outpost`) |
| --- | --- | --- | --- |
| variant / directory / Gradle path | as given | `rust-outpost` | `kotlin-outpost` |
| display name | title case of the name (`--display-name` overrides) | `Rust Outpost` | `Kotlin Outpost` |
| package suffix (`{{PKG}}`) | name minus `-outpost`, hyphens removed (`--package` overrides) | `rust` | `kotlin` |
| package path (`{{PKG_PATH}}`) | package suffix with dots as directories | `rust` | `kotlin` |
| app symbol | PascalCase of the name | `RustOutpost` | `KotlinOutpost` |
| Gradle accessor (`{{CAMEL}}`) | camelCase of the symbol | `rustOutpost` | `kotlinOutpost` |
| iOS framework | symbol + `Shared` | `RustOutpostShared` | `KotlinOutpostShared` |
| resources package | `outpost.<name with _>.shared.generated.resources` | `outpost.rust_outpost.shared.generated.resources` | `outpost.kotlin_outpost.shared.generated.resources` |
| iOS bundle id | `dev.appoutlet.outpost.<last package segment in PascalCase>` | `dev.appoutlet.outpost.Rust` | `dev.appoutlet.outpost.Kotlin` |

Android uses `dev.appoutlet.outpost.<suffix>` (application id and shared namespace) and
`dev.appoutlet.outpost.<suffix>.android` (app namespace).

Two overrides exist for names the rules cannot guess:

- `--package javascript` on `js-outpost` keeps the package at `dev.appoutlet.outpost.javascript`
  while the symbol stays `JsOutpost`; `--package community.rust` produces
  `dev.appoutlet.outpost.community.rust` with the sources under `.../community/rust/`.
- `--platforms android,desktop,ios` (default all three) picks the app modules.

## Generated tree

With all platforms selected, for a variant named `rust-outpost`:

```
<rust-outpost>/
├── .run/Desktop - Hot reload (Rust Outpost).run.xml       (desktop module)
├── android/                                                (android module)
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── kotlin/dev/appoutlet/outpost/rust/android/MainActivity.kt
│       └── res/values/strings.xml
├── desktop/                                                (desktop module)
│   ├── build.gradle.kts
│   └── src/main/kotlin/dev/appoutlet/outpost/rust/desktop/Main.kt
├── ios/                                                    (ios module)
│   ├── Configuration/Config.xcconfig
│   ├── Rust Outpost.xcodeproj/
│   │   ├── project.pbxproj
│   │   ├── project.xcworkspace/contents.xcworkspacedata
│   │   └── xcshareddata/xcschemes/Rust Outpost.xcscheme
│   └── Rust Outpost/
│       ├── ContentView.swift
│       ├── Info.plist
│       ├── iOSApp.swift
│       ├── Assets.xcassets/{Contents.json,AppIcon.appiconset,AccentColor.colorset}
│       └── Preview Content/Preview Assets.xcassets/Contents.json
└── shared/                                                 (always)
    ├── build.gradle.kts
    └── src/
        ├── commonMain/composeResources/values/strings.xml
        ├── commonMain/kotlin/dev/appoutlet/outpost/rust/RustOutpostApp.kt
        ├── commonTest/kotlin/dev/appoutlet/outpost/rust/RustOutpostAppTest.kt
        └── iosMain/kotlin/dev/appoutlet/outpost/rust/MainViewController.kt
```

Every platform entry point calls the same `RustOutpostApp()` composable, which is the only place the
variant touches the platform core:

```kotlin
@Composable
fun RustOutpostApp() {
    App(config = outpostConfig { app { title = ...; headline = ... } })
}
```

`settings.gradle.kts` gains one include per generated module, inserted before
`include(":outpost-core")`:

```kotlin
include(":rust-outpost:android")
include(":rust-outpost:desktop")
include(":rust-outpost:shared")
```

## How the files are produced

- `templates/manifest.txt` maps `module | source | destination`. The module column is one of
  `shared`, `android`, `desktop`, `ios`: `shared` is always written, the platform modules only when
  `--platforms` selects them, and `desktop` also owns the IDE run configuration.
- Destinations and file contents are rendered with the placeholders `{{NAME}}`, `{{DISPLAY}}`,
  `{{PKG}}`, `{{PKG_PATH}}`, `{{SYMBOL}}`, `{{CAMEL}}`, `{{FRAMEWORK}}`, `{{RES_PKG}}` and
  `{{BUNDLE_ID}}`. A rendered file that still contains `{{` is a hard error.
- App versions come from the version catalog, never from the templates: the generated
  `android/build.gradle.kts` reads `libs.versions.outpost.code` / `libs.versions.outpost.name`, and the
  generated `desktop/build.gradle.kts` reads `libs.versions.outpost.name` (package version) and
  `libs.versions.outpost.uuid` (Windows `upgradeUuid`), so every variant tracks the reference app.
- Sources prefixed with `repo/` are copied from the repository and additionally rewritten with the
  reference tokens: `KotlinOutpostShared` -> framework, `Kotlin Outpost` -> display name,
  `kotlin-outpost` -> variant name, `dev.appoutlet.outpost.Kotlin` -> bundle id.
- Only `project.pbxproj` is a `repo/` source. Xcode owns that format (object version 77 with
  synchronised groups), so the scaffold transforms the reference project instead of hand-maintaining
  a copy; the shared object ids, including the target blueprint `CF343C86E05D89F53944F868` used by
  the generated scheme, come from there.
- The shared module always declares the reference target set (`android`, `jvm`, `iosArm64`,
  `iosSimulatorArm64`), so a platform left out of `--platforms` can be added later by generating
  that app module only.
- Running the script twice with the same name fails before writing anything; it never overwrites an
  existing file, and it refuses a variant whose package is already used elsewhere in the build.

## Deliberate limits

- **Brand assets are not generated.** No Android `mipmap` icons (the manifest has no `android:icon`,
  so Android falls back to its default icon), no iOS `AppIcon` images (the app icon set is present
  but empty, like a fresh Xcode project), no desktop `icon.icns`/`icon.ico`/`icon.png`. The scaffold
  must not ship `kotlin-outpost` branding, and issue #39 puts icons out of scope; the follow-up is to
  add real assets, following the layout in `docs/kotlin-outpost/icon/`.
- **No theme and no content.** The generated app uses `OutpostConfig.Theme.Default` and declares no
  content sources. The variant owner adds `theme { seedColor = ... }` and `content { ... }`.
- **No screens or features.** Everything user-facing stays in `:outpost-core`.
- **No `README`/`docs` entry.** `docs/<variant>/<variant>.md` and the README run commands are left to
  the variant owner, mirroring `docs/kotlin-outpost/kotlin-outpost.md`.
- **Platforms are bounded by the core.** A variant can only ship platforms `:outpost-core` builds
  (`android`, `jvm`, `iosArm64`, `iosSimulatorArm64`); a web or server variant needs a core target
  first.
- **The iOS Xcode project cannot be validated on Linux.** The scaffold was verified by compiling the
  Kotlin side (both Apple targets), running the generated `jvmTest`, compiling the desktop module and
  the Android sources plus resources; opening a generated project in Xcode is the first macOS-side
  check.

## Verification performed when this skill was added

Three throwaway variants were scaffolded and compiled with the focused tasks, then removed again —
none of them is committed, the scaffold is the deliverable:

```
rust-outpost (all platforms)         :rust-outpost:shared:jvmTest                          -> RustOutpostAppTest: 1 test, 0 failures
                                     :rust-outpost:desktop:compileKotlin                   -> BUILD SUCCESSFUL
                                     :rust-outpost:shared:compileKotlinIosArm64            -> BUILD SUCCESSFUL
                                     :rust-outpost:android:compileDebugKotlin
                                       :rust-outpost:android:processDebugResources         -> BUILD SUCCESSFUL
mycontent --platforms desktop        :mycontent:shared:jvmTest                             -> MycontentAppTest: 1 test, 0 failures
                                     :mycontent:desktop:compileKotlin                      -> BUILD SUCCESSFUL
                                     (no android/ or ios/ directory; settings.gradle.kts only gained
                                      the desktop and shared includes)
community-outpost --package
  community.rust --platforms android :community-outpost:shared:jvmTest                     -> CommunityOutpostAppTest: 1 test, 0 failures
                                     (sources under .../community/rust/, 9 files, android only)
```

For the reference-shaped variant the earlier run also produced a debug APK
(`:javascript-outpost:android:assembleDebug` -> `android-debug.apk`, 16.8 MB) and `aapt2 dump
badging` reported `package: name='dev.appoutlet.outpost.javascript'`,
`application: label='Javascript Outpost' icon=''` and
`launchable-activity: dev.appoutlet.outpost.javascript.android.MainActivity`, which is the launcher
entry point the scaffold generates.

The generated modules were compiled for a full platform set (`rust-outpost`), for a desktop-only
subset (`mycontent`) and for an Android-only subset with a custom package (`community-outpost`), so
every module type the scaffold can generate — shared, android, desktop and the Apple source sets —
has been compiled at least once.
