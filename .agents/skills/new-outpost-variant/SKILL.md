---
name: new-outpost-variant
description: Use when the user asks to create, scaffold or add a new Outpost variant, sub app or bridge app for any brand (for example rust-outpost, js-outpost, mycontent). Generates the android, desktop, shared and ios modules that fit the variant, wires settings.gradle.kts and verifies the variant compiles. MUST NOT be used for features inside an existing variant or inside :outpost-core.
---

# New Outpost Variant

Outpost is a white-label platform: `:outpost-core` owns the UI, the state, the theming and the
content model, while a *variant* is a thin bridge that packages that core as an app for a brand.
`kotlin-outpost` is the reference variant, and a new variant must end up structurally identical to
it, whatever the brand and whatever the name.

The scaffold is mechanical, so this skill ships a generator with the templates next to it:
`scripts/scaffold-variant.sh`. Run it instead of retyping files.

## Workflow

### 1. Get the variant name

If the user did not give a name, ASK for it before running anything, then derive the directory name
from the brand: `Rust Outpost` -> `rust-outpost`, `My Content` -> `mycontent`. The name is the
directory and the Gradle path, so it MUST be kebab-case, lowercase alphanumeric with single
hyphens, and unique. Nothing about the scaffold is tied to a name, a brand or a platform set.

### 2. Preview what will be created

```bash
.agents/skills/new-outpost-variant/scripts/scaffold-variant.sh rust-outpost --dry-run
.agents/skills/new-outpost-variant/scripts/scaffold-variant.sh mycontent --platforms desktop --dry-run
```

The dry run prints the derived identifiers (package, symbol, iOS framework, resources package,
bundle id) and every file that would be written; nothing is written and `settings.gradle.kts` is
left alone. Read the identifiers back to the user when the name is ambiguous, and stop if they
disagree.

### 3. Scaffold

```bash
.agents/skills/new-outpost-variant/scripts/scaffold-variant.sh rust-outpost
```

Options, all optional:

| Option | Use it when |
| --- | --- |
| `--display-name "Rust Outpost"` | the human name is not the title-cased variant name (`Rust Outpost` is the default for `rust-outpost`). |
| `--platforms android,desktop,ios` | the variant ships a subset of the platforms (default: all three). `shared` is always generated because every platform entry point goes through it; the shared bridge keeps the reference target set, so a platform left out now can be added later. |
| `--package <suffix>` | the name does not imply the package, for example `js-outpost --package javascript` or `mycontent --package community.rust` (default: the name without `-outpost` and hyphens). |

It creates `<variant>/{shared,android,desktop,ios}` for the selected modules plus `.run/`, and
inserts the matching includes into `settings.gradle.kts` before `include(":outpost-core")`. It
refuses to overwrite an existing variant or an existing package, so a failure here means the name or
package is taken, not that the scaffold is broken.

### 4. Verify (mandatory, focused tasks only)

```bash
./gradlew :rust-outpost:shared:jvmTest                        # variant boots the core UI
./gradlew :rust-outpost:desktop:compileKotlin                 # desktop entry point
./gradlew :rust-outpost:android:compileDebugKotlin :rust-outpost:android:processDebugResources
./gradlew :rust-outpost:shared:compileKotlinIosArm64           # iOS source set + framework target
```

The scaffold prints exactly the commands that match the modules it generated; run those.

- `jvmTest` runs the generated `should load app` test, which asserts the core navigation renders,
  so it is the cheapest proof that the bridge wires `:outpost-core` correctly.
- Apple targets cross-compile on Linux, so the `iosArm64`/`iosSimulatorArm64` klibs are real local
  evidence; only the Xcode build and `iosSimulatorArm64Test` need macOS.
- If the Gradle daemon dies on a low-memory machine, run the commands one at a time instead of in
  one invocation.
- Full CI coverage (`detekt`, `allTests`, `koverVerify`) picks the new modules up automatically
  through `settings.gradle.kts`; do not run those locally unless the user asks.

### 5. Report, then hand over the brand work

Report the derived identifiers, the files created and the exact compile results. Then state the
follow-ups this skill deliberately leaves to the variant owner: brand assets (launcher icons for
Android/iOS/desktop), the variant theme seed colour, its content sources, and its `README`/`docs/`
entry. See [`references/generated-structure.md`](./references/generated-structure.md) for the
generated layout, the identifier and placeholder tables, and the known limits.

## HARD RULES

1. MUST NOT run `./gradlew build`, `./gradlew assemble`, `allTests` or any other whole-project task:
   the repository forbids them and the focused tasks above are sufficient.
2. MUST keep the variant thin. Feature screens, dynamic theming, icons and content feeds are out of
   scope for the scaffold; never invent brand values, and never copy `kotlin-outpost`'s Kotlin
   ecosystem feeds, icons or colours into another variant.
3. MUST delegate to the core by calling `App(config = outpostConfig { ... })` from
   `dev.appoutlet.outpost`; MUST NOT fork, copy or edit `:outpost-core` to make a variant fit.
4. MUST NOT scaffold a variant by hand: hand-written modules drift from `kotlin-outpost` (plugins,
   `detekt` blocks, iOS project settings), which is exactly what this skill prevents.
5. MUST NOT edit `kotlin-outpost` while scaffolding another variant. `kotlin-outpost` is the
   reference: the scaffold transforms it, so changes to it change every future variant.
6. MUST run the step 4 commands before claiming the variant builds.
7. MUST only generate the platforms the core can build: variants package `:outpost-core`, whose
   targets are Android, JVM (desktop) and `iosArm64`/`iosSimulatorArm64`. A brand that needs another
   target (web, a server, a second Apple arch) needs a core target first — say so instead of
   promising a module the scaffold cannot support.

## Maintaining this skill

- `scripts/scaffold-variant.sh` derives every identifier (package, symbol, framework, resources
  package, bundle id) from the variant name and options, then renders `templates/manifest.txt`,
  whose first column selects the module each file belongs to.
- Templates are the source of truth for the Gradle and source files; the iOS `project.pbxproj` is
  transformed from `kotlin-outpost/ios` because Xcode owns that format. Keep both in sync when
  `kotlin-outpost` changes.
- `evals/evals.json` holds the prompts used to check that this skill triggers and scaffolds
  correctly, including names, package overrides and platform subsets that have nothing to do with
  the Kotlin Outpost brand.
