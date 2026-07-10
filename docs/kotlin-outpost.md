# Kotlin Outpost

Kotlin Outpost is the first concrete application built on top of the reusable `:outpost` module. It packages the shared Outpost experience into platform-specific apps for Android, iOS, and desktop.

Kotlin Outpost is intended to be a dedicated hub for the Kotlin ecosystem, bringing together high-signal content, community activity, and discovery features in one place.

Its role in the repository is intentionally thin:

- `kotlin-outpost/shared/` depends on `:outpost` and exposes `KotlinOutpostApp()`
- Android launches that shared app from `MainActivity`
- Desktop launches it from `main()` in the desktop module
- iOS exposes it through `MainViewController()` for the Xcode app

In practice, Kotlin Outpost is the reference implementation of the Outpost platform. It shows how the shared module is embedded in real applications, while keeping most of the product UI and business logic inside the reusable `:outpost` module.

This separation makes it easier to evolve Outpost as a white-label product: shared functionality stays in `:outpost`, while app-specific packaging, branding, and platform startup code live in the Kotlin Outpost modules.

## Planned Features

- Articles from curated blogs
- Videos from curated YouTube channels
- Kotlin-related podcasts
- A list of the best Kotlin repositories
- Trending Kotlin repositories
- Community discussions from Reddit, Stack Overflow, and Twitter
- A job board for Kotlin roles
