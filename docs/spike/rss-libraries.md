# RSS and Atom libraries for the Articles feature

Findings for [Outpost #41 — Spike: Libraries for fetching RSS feeds](https://github.com/MessiasLima/Outpost/issues/41).
The goal is a library Outpost can use from `commonMain` to pull article metadata (and content) from
external RSS 2.0 / Atom feeds on Android, iOS and Desktop, tolerant of the malformed feeds that
exist in the wild.

## Decision

**Use [`com.prof18.rssparser:rssparser`](https://github.com/prof18/RSS-Parser) 6.1.8 from `commonMain`.**

It is a Kotlin Multiplatform *parsing* library: it publishes real variants for every target Outpost
builds (`android`, `jvm`, `iosArm64`, `iosSimulatorArm64`), handles RSS 2.0, Atom 1.0 and RDF in one
model, and needs no platform-specific source set for the basic path (`RssParser()` + `parse(xml)` are
common code). The trade-offs — dates stay raw strings, feeds are not cached, and the transport is the
library's own OkHttp/NSURLSession rather than a client Outpost owns — are all mitigable and are
listed in [Risks](#risks).

Approaches rejected in this spike:

- **Hand-written XML model over a raw HTTP client** (Ktor + `xmlutil`): works for RSS 2.0, but the
  project would own the model, the namespace handling and the format divergence surface forever.
  See [Approach B](#b-raw-http-client--hand-written-xml-model).
- **A high-level feed-management library**: none exists for Kotlin Multiplatform. Every "batteries
  included" feed reader in the ecosystem is either an application or a JVM-only library. See
  [Approach C](#c-high-level-feed-management-library).

## What the Articles feature needs

- Runs in `commonMain` of `:outpost-core` (targets: `android`, `jvm`, `iosArm64`,
  `iosSimulatorArm64`; Kotlin 2.4.20, AGP 9.4.1, `compileSdk 37`, `minSdk 24`, JVM target 11).
- Handles **RSS 2.0** and **Atom**, and tolerates the malformed markup real blogs emit.
- Extracts per item: title, description/content, link, publication date, enclosures (audio/video),
  categories, GUID; per channel: title, description, link, image.
- Must not crash the app on a broken feed or on a non-feed response (HTML error pages are common).
- Feed configuration already exists: `Articles` / `RssFeed(url)` in
  `../../outpost-core/src/commonMain/kotlin/dev/appoutlet/outpost/core/config`.
- `libs.versions.toml` currently has **no HTTP client at all** — no Ktor, no OkHttp — so a
  dependency that brings its own transport is a smaller change than one that requires choosing and
  wiring a client first.

## How this spike was evaluated

A throwaway Gradle probe (not committed) reproduced Outpost's setup exactly — Gradle 9.8.0, Kotlin
2.4.20, AGP 9.4.1 `com.android.kotlin.multiplatform.library`, `compileSdk 37`, `minSdk 24`,
JVM target 11, targets `jvm()`, `android`, `iosArm64()`, `iosSimulatorArm64()` — and was driven with
real feeds, not fixtures:

| Sample | Origin | Size | Shape |
| --- | --- | --- | --- |
| `rss20-blog.xml` | `https://blog.jetbrains.com/kotlin/feed/` | 183,619 B | WordPress RSS 2.0, `content:`, `dc:`, `wfw:` namespaces |
| `atom-blog.xml` | `https://github.blog/feed/atom/` | 1,527,471 B | Atom 1.0, `thr:` namespace, `rel="alternate"` links |
| `podcast.xml` | `https://changelog.com/podcast/feed` | 6,264,762 B | Podcast RSS 2.0, `itunes:`/`content:`/`atom:` namespaces, 1,015 items, `enclosure` |
| `malformed.xml` | hand-written | 598 B | Unescaped `&`, undefined entity `&eacute;`, junk `pubDate` |

Commands (focused tasks only, as `../../AGENTS.md` requires):

```bash
./gradlew :rssparser-probe:compileCommonMainKotlinMetadata :rssparser-probe:compileKotlinJvm \
          :rssparser-probe:compileAndroidMain :rssparser-probe:compileKotlinIosArm64 \
          :rssparser-probe:compileKotlinIosSimulatorArm64
./gradlew :rssparser-probe:jvmRun --args="samples/rss20-blog.xml samples/atom-blog.xml samples/podcast.xml samples/malformed.xml https://blog.jetbrains.com/kotlin/feed/"
./gradlew :rssparser-probe:jvmTest
./gradlew :rssparser-probe:dependencies --configuration iosArm64CompileKlibraries
```

Host limits worth knowing: Apple targets **compile** to klibs on Linux (all five compile tasks above
succeeded here), but Apple *tests* cannot run — the Kotlin plugin reports `Native task
'iosSimulatorArm64Test' is disabled … simulator tests require macOS`. iOS behaviour therefore needs
a macOS runner or a Mac; CI already runs `./gradlew allTests -Pkover koverVerify` on ubuntu, where
Apple test tasks are skipped.

## A. Dedicated KMP parsing library — `com.prof18.rssparser:rssparser`

`RSS-Parser` (`prof18/RSS-Parser`, Apache-2.0, Marco Gomiero) is the reference KMP feed parser: it is
the parser behind FeedFlow, ReLabs and CapyReader. Latest release at the time of writing: **6.1.8
(2026-07-30)**; actively maintained (Renovate-driven dependency bumps, several 6.1.x releases in
2026). Built with Kotlin 2.4.10, consumed here by Kotlin 2.4.20 without warnings.

### Verified KMP coverage

Both from the published Gradle module metadata and from local compilation:

| Target | Published variant (`rssparser-6.1.8.module`) | Compiled locally |
| --- | --- | --- |
| common metadata | `metadataApiElements` | ✅ `:compileCommonMainKotlinMetadata` |
| Android | `androidApiElements-published` (`androidJvm`, AAR) | ✅ `:compileAndroidMain` |
| Desktop JVM | `jvmApiElements-published` | ✅ `:compileKotlinJvm` |
| iOS device | `iosArm64ApiElements-published` | ✅ `:compileKotlinIosArm64` (klib) |
| iOS simulator | `iosSimulatorArm64ApiElements-published` | ✅ `:compileKotlinIosSimulatorArm64` (klib) |
| also published | `iosX64`, `macosArm64/X64`, `tvos*`, `watchos*`, `js`, `wasmJs` | not needed by Outpost |

The iOS klib resolves without any Apple-host dependency:

```
iosArm64CompileKlibraries
+--- org.jetbrains.kotlin:kotlin-stdlib:2.4.20
\--- com.prof18.rssparser:rssparser:6.1.8
     \--- com.prof18.rssparser:rssparser-iosarm64:6.1.8
          +--- org.jetbrains.kotlin:kotlin-stdlib:2.4.10 -> 2.4.20
          \--- org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0
               \--- …coroutines-core-iosarm64:1.11.0
```

### Observed parsing behaviour (JVM probe, real feeds)

```
--- file rss20-blog.xml (732ms)   items = 12
    channel.lastBuild = Thu, 15 Oct 2026 17:48:30 +0000
    item[0].title = Google Summer of Code 2026 With the Kotlin Foundation: Recap
    item[0].pubDate = Thu, 15 Oct 2026 15:03:05 +0000      <- RFC-822 string, not a date type
    item[0].link  = https://blog.jetbrains.com/kotlin/2026/10/google-summer-of-code-2026-with-kotlin/
    item[0].cats  = [education, news, gsoc, open-source]
    item[0].descLen = 397                                  <- HTML, not plain text
--- file atom-blog.xml (174ms)    items = 10
    channel.lastBuild = 2026-10-15T21:53:32Z               <- ISO-8601 for Atom
    item[0].link = https://github.blog/news-insights/company-news/the-next-developer-interface-is-ambient-intelligence/
    item[0].descLen = 425
--- file podcast.xml (548ms)      items = 1015
    item[0].audio    = https://op3.dev/e/https://pscrb.fm/rss/p/https://cdn.changelog.com/uploads/podcast/686/the-changelog-686.mp3
    item[0].rawEncl. = RawEnclosure(url=…/the-changelog-686.mp3, length=234654580, type=audio/mpeg)
--- file malformed.xml (26ms)     items = 1
    channel.title = Malformed & Broken Feed               <- repaired and decoded
    item[0].pubDate = "Not a date at all"                 <- kept verbatim, never validated
--- url https://blog.jetbrains.com/kotlin/feed/ (418ms)    <- getRssChannel(url) over the network
```

Also verified by `:rssparser-probe:jvmTest` (6 tests, all green): RSS 2.0, Atom, podcast enclosure,
malformed-feed repair, raw-date behaviour, and non-feed payload handling.

### API and transport

- `RssParser()` is an `expect fun` in the library: usable from `commonMain` with zero platform code.
- `suspend fun parse(rawRssFeed: String): RssChannel` — parse anything Outpost fetched itself.
- `suspend fun getRssChannel(url: String): RssChannel` — the library fetches and parses.
- Platform builders for custom transports: `RssParserBuilder(callFactory: Call.Factory, charset:
  Charset?)` on Android/JVM (OkHttp), `RssParserBuilder(nsUrlSession: NSURLSession)` on Apple (shared
  session by default), Ktor `HttpClient` on web.
- Error surface: `HttpException(code, message)` for non-2xx, `RssParsingException` when SAX parsing
  fails twice; the second attempt runs `escapeInvalidXmlEntities(...)`, a regex repair pass for
  standalone `&`, duplicate closing tags, empty/void tags — which is why `malformed.xml` parsed.
- **Trap found:** valid XML that is not a feed (an HTML error page) does not produce
  `RssParsingException`. It escapes as `java.lang.IllegalArgumentException: Required value was null.`
  from `SaxFeedHandler.getChannel()` (`JvmXmlParser.kt:61`). Any sync path must catch `Throwable`, or
  a captive-portal/404-HTML page crashes the Articles refresh.

### Footprint

| Artifact | Size |
| --- | --- |
| `rssparser-jvm-6.1.8.jar` | 122,569 B |
| `rssparser-iosarm64-6.1.8.klib` | 134,400 B |
| `okhttp-jvm-5.4.0.jar` (Android/JVM only) | 863,503 B |
| `okio-jvm-3.16.0.jar` (Android/JVM only) | 382,591 B |

Android/JVM pull OkHttp 5.4.0 (`okhttp-android`/`okhttp-jvm` + `okhttp-coroutines`) and
`kotlinx-coroutines-core`; Apple pulls only coroutines. For comparison, the Ktor client alone is
`ktor-client-core-jvm` 963,192 B. No `kotlinx-datetime`, no serialization, no reflection.

## B. Raw HTTP client + hand-written XML model

Approach: Ktor `HttpClient(OkHttp)` for transport plus `@Serializable` models of the feed XML decoded by
`io.github.pdvrieze.xmlutil:serialization` 1.0.2 (optionally through
`io.ktor:ktor-serialization-kotlinx-xml` 3.6.0, which itself pins xmlutil **0.91.3** — a version
skew to be aware of if both are used).

This variant was built and run as a second probe module (`:ktor-probe`) against the same four
samples, with the same toolchain. It compiles for metadata/JVM/Android and resolves for
`iosArm64CompileKlibraries` (`xmlutil serialization-iosarm64:1.0.2`, `core-iosarm64:1.0.2`, the Ktor
iOS variants).

**What it cost to make it work at all** — the API is annotation-dense and the defaults are hostile to
feed XML:

- Format config that compiles and parses: `XML.v1 { repairNamespaces = true; setIndent(0); policy { ignoreUnknownChildren() } }`.
  `@XmlSerialName` only has `value`/`namespace`/`prefix`; `ignoreUnknownChildren()` is on
  `DefaultXmlSerializationPolicy.Builder` (reachable through `policy { … }`), and there is no
  `POLICY_UNCHECKED`.
- Element names must be declared on the **class** (`@XmlSerialName("channel"|"item"|"enclosure")`),
  because a class-typed element is otherwise named after its *type*.
- Every text child needs `@XmlElement(true)`, because the default primitive output kind is
  `OutputKind.Attribute`.

**Observed results (same CLI, same samples as approach A):**

```
--- rss20-blog.xml (194ms, 183134 chars)   items = 12
    channel.title = Kotlin : A concise multiplatform language developed by JetBrains | The JetBrains Blog
    first.pubDate = Thu, 15 Oct 2026 15:03:05 +0000
    first.enclos. = null
--- atom-blog.xml (2ms)  FAILED
    nl.adaptivity.xmlutil.XmlException: Unknown position - Local name "{http://www.w3.org/2005/Atom}feed"
    for root tag does not match expected name "rss"
--- podcast.xml (549ms, 6246973 chars)     items = 1015
    first.enclos. = Enclosure(url=…/the-changelog-686.mp3, length=234654580, type=audio/mpeg)
--- malformed.xml (0ms)  FAILED
    nl.adaptivity.xmlutil.XmlException: 4:21 - Missing text when attempting to retrieve it
```

- **Atom forces a second model** (the root name mismatch is fatal, and pinning the root name only
  moves the failure to "Field 'channel' is required … but it was missing"). A three-class, 52-line
  Atom model does parse the 1.5 MB GitHub feed, so the cost is duplicated modelling, not a wall.
- **No malformed-feed tolerance**: the sample that approach A repairs throws, with no partial
  object, and the same under the platform and generic parsers.
- **Extension elements are lost silently**: the default handler throws on unknown elements, and so
  does `ignoreNamespaces()` (proved on same-namespace unknowns and unknown attributes); the only
  setting that recovers is `ignoreUnknownChildren()`, which silently drops `itunes:*`, `content:*`
  and `media:*`. A missing/wrong annotation therefore yields `null` fields instead of an error.
- Size: 4 data classes / 49 non-comment lines for RSS; ~130 lines / 7 classes for both models
  (~87 non-comment), before any recovery, date handling or tests.

## C. High-level feed management library

The ticket asked for a high-level feed-management option (fetch + store + refresh). **No such
library exists for Kotlin Multiplatform** — this is a negative result, verified against Maven Central:

| Candidate | Verdict |
| --- | --- |
| FeedFlow (`prof18/feed-flow`) | The closest thing to what we want, but it is an **application**, not a published library. Its stack is exactly `rssparser` + its own Room database + its own sync — i.e. what Outpost's #42–#45 already plan to build for itself. |
| `com.ouattararomuald:syndication` 2.1.1 | Kotlin, RSS 2.0 + Atom 1.0, OkHttp + `simple-xml`. Publishes a **plain jar with no Gradle/KMP metadata** → JVM/Android only; Maven Central last update **2021-05-30**; pins `kotlin-stdlib` 1.4.32 and `okhttp` 5.0.0-alpha.2. Cannot live in `commonMain`. |
| `com.apptasticsoftware:rssreader` 3.9.3 | Java library, `org.gradle.jvm.version: 11`, no Kotlin platform variants → JVM only (Desktop). Would need a full parallel implementation for iOS/Android. Last updated 2025-12-01, so maintained, but single-platform. |
| `com.rometools:rome` 2.1.0 (last release 2023-03) | Java/JVM only, same limitation as above. The classic answer for server-side Java, irrelevant for `commonMain`. |
| `com.icure:rssparser` (listed as a candidate in #41) | **Does not exist.** The `com.icure` group on Maven Central publishes 65 artifacts (Cardinal SDK, Charix, FHIR models, …) and none of them is an RSS parser; a keyword search for `icure rss` also returns nothing. Keep this out of the evaluation. |

Consequence for planning: the feed *management* layer (persistence, refresh policy, dedupe, retry)
is Outpost's own work regardless of which parsing library is chosen — that is the entire content of
tickets #42–#45, and this spike does not change them.

## Comparison

| Dimension | A. `rssparser` 6.1.8 | B. Ktor + hand-written xmlutil model | C. high-level manager |
| --- | --- | --- | --- |
| Android / iOS / Desktop from `commonMain` | yes, published variants + locally compiled | depends on hand-written model, xmlutil resolves for all targets | none exists |
| RSS 2.0 + Atom + RDF in one call | 4/4 samples parsed with one model | RSS 2.0 only via one model; Atom needs separate namespaced model | n/a |
| Date handling | raw `String?` (both RSS and Atom formats) — Outpost must parse | raw strings too, same work | — |
| Enclosures / media | `rawEnclosure(url,length,type)`, `audio`, `video`, `image`, `rawMediaContent` | must be modelled by hand | — |
| Malformed feeds | one internal repair pass; `malformed.xml` parsed | throws `XmlException`, no repair, no partial object | — |
| Error surface | `HttpException`, `RssParsingException`, plus a raw `IllegalArgumentException` trap | `XmlException` (thrown for malformed *and* for wrong-format roots); silent `null`s when recovery is enabled | — |
| Caching / refresh | none by design (library is parsing-only) | none | — |
| Code Outpost owns | ~0 lines of parsing; model mapping only | ~130 lines of models + format policy + recovery + tests | — |
| Extra deps on Android/JVM | OkHttp 5.4.0 + Okio (~1.2 MB) | Ktor 3.6.0 + xmlutil 1.0.2 (Ktor core alone 963 KB) | — |
| Maintenance | 6.1.8 / 2026-07-30, 3 production apps depending on it | Outpost owns the maintenance | — |

## Recommendation

Adopt **`com.prof18.rssparser:rssparser` 6.1.8** in `:outpost-core` `commonMain`:

1. It is the only candidate that satisfies all three required targets from shared code today, and it
   was verified compiling for each of them with Outpost's exact toolchain.
2. The hard parts of feed parsing — RSS/Atom/RDF divergence, namespace prefixes (`itunes:`, `media:`,
   `content:`, `dc:`), malformed-markup repair, Atom `rel="alternate"` link selection — are already
   solved and battle-tested by FeedFlow et al. Re-implementing them with xmlutil is ongoing
   maintenance with no product value.
3. It adds no serialization/date/DI machinery: Outpost keeps owning persistence, refresh and the
   domain model, exactly as tickets #42–#45 assume.
4. If Outpost later adds Ktor for other features (#70, #74), the migration is one function call:
   fetch with the shared client and call `parser.parse(body)` — no model rewrite.

## Gradle dependencies and source-set configuration

```toml
# gradle/libs.versions.toml
[versions]
rssparser = "6.1.8"

[libraries]
rssparser = { module = "com.prof18.rssparser:rssparser", version.ref = "rssparser" }
```

```kotlin
// outpost-core/build.gradle.kts — inside kotlin { sourceSets { … } }
commonMain.dependencies {
    implementation(libs.rssparser)
}
```

That is all that is required for parsing. Notes:

- The `android`/`jvm` variants bring OkHttp transitively; the Apple variants do not. Nothing to add
  to `jvmMain`/`androidMain` for the default path.
- `RssParser()` works from `commonMain`, so **no** platform source set is needed. Add one only if
  Outpost wants to inject its own transport (shared `OkHttpClient` with timeouts/UA on Android/JVM,
  a configured `NSURLSession` on Apple) — that needs a new `outpost-core/src/iosMain/kotlin` source
  set and an `expect/actual fun outpostRssParser(): RssParser` in `commonMain`:
  - `androidMain`/`jvmMain`: `RssParserBuilder(callFactory = sharedOkHttpClient).build()`
  - `iosMain`: `RssParserBuilder(nsUrlSession = configuredSession).build()`
- Tests: `commonTest` can call `parse(feedXmlString)` with fixture strings, so the parsing/mapping
  coverage (kover gate: 80 %) needs no network and runs on every target.

## Migration path

| Step | Ticket |
| --- | --- |
| Wire `rssparser` into `:outpost-core`, add an article domain model + mapper from `RssChannel`/`RssItem` (dates normalised at the boundary) | #42 (Room + single-feed sync) / follow-up hardening ticket |
| Persist articles, dedupe by GUID, refresh a single configured `RssFeed` | #42 |
| Background scheduling for feed refresh | #43 (decision), #44 (executor), #45 (24-hour service) |
| Article list / detail UI, favorites | #46–#52 |

## Risks

**Malformed feeds.** The library's repair pass is regex-based (`escapeInvalidXmlEntities`) and only
runs after a first failure; it fixed our `malformed.xml`, but feeds that fail both attempts throw
`RssParsingException`. Sync must treat a failing feed as a per-feed error, keep the cached articles
and never let one bad feed abort the others.

**Non-feed payloads are not classified.** As above: HTML/garbage XML that parses as well-formed XML
surfaces as `IllegalArgumentException("Required value was null.")`. Recommended defence: catch
`Throwable` in the sync use case, and add a cheap pre-check (body starts with `<` + contains
`<rss`/`<feed`/`<rdf:RDF`) before calling the parser.

**Dates are unvalidated strings.** `RssItem.pubDate` / `RssChannel.lastBuildDate` are `String?` —
`"Not a date at all"` was returned verbatim. RSS is RFC-822 (`Thu, 15 Oct 2026 15:03:05 +0000`) and
Atom is RFC-3339 (`2026-10-15T19:39:27Z`), so Outpost needs a small multi-format parser at the sync
boundary (kotlinx-datetime), with a documented fallback (e.g. `null` + "unknown date" in the UI, or
`lastBuildDate`) instead of trusting the feed.

**Large payloads.** The 6 MB, 1,015-item podcast feed parsed in 548 ms and the 1.5 MB Atom feed in
174 ms on the JVM (cold, single-shot) — acceptable, but the whole document is read into memory and
the model materialises every item. On iOS/arm64 or a low-end Android device expect more, and a
hostile feed could be arbitrarily large: cap the response size (e.g. 5–10 MB) and consider a page
size for persistence. Nothing in the library enforces a limit.

**Transport duplication.** The default path gives Android/JVM a private `OkHttpClient` (no request
headers beyond OkHttp's default `User-Agent`, 10 s OkHttp timeouts) and iOS the shared
`NSURLSession` (UTF-8 assumed on the repair path). Sites that block default user agents or need
custom headers, proxies or certificate pinning require the platform builders — and then Outpost has
two HTTP stacks if it later adopts Ktor. Decide explicitly whether to inject a shared client
(`expect/actual`) or to fetch with a future shared client and call `parse(String)`.

**Version coupling.** The library is compiled against Kotlin 2.4.10; Kotlin 2.4.20 consumes its klibs
fine (verified). A jump to a newer Kotlin line before an `rssparser` release should be checked
against the library's own Kotlin bump, especially for the Apple klibs.

**Content is HTML.** `description`/`content` are raw HTML of arbitrary length — the Article screen
(#48) needs sanitised rendering; the library does not strip or normalise it.

**No caching by design.** 6.x removed feed caching: every refresh is a network round trip, and
deduplication is the caller's job. That is consistent with #42 but should be stated in the sync
design.

**Single-maintainer dependency** (Apache-2.0, one primary author). Mitigation options if it ever
stalls: pin the version (it is a parsing library, not a moving target), or fork — the parser has no
exotic dependencies, so a fork is cheap.

## Follow-up tickets

Already in the backlog and directly usable with this choice: #42 (Room + single-feed sync, explicitly
depends on #41), #43 (background scheduling spike), #44 (task executor), #45 (24-hour enqueue
service).

New ticket created from this spike: [#109 — Harden the RSS parsing layer: dates, non-feed payloads,
size limits](https://github.com/MessiasLima/Outpost/issues/109) (date normalisation for RFC-822 and
RFC-3339, defence against non-feed payloads, response size caps, and committed malformed-feed
fixtures for `commonTest`).

## Open questions

- Does Outpost want a single shared HTTP client (OkHttp/NSURLSession/Ktor) injected into the parser,
  or should the library keep its own transport until a shared client exists?
- Which fallback should the UI show when `pubDate` cannot be parsed — `lastBuildDate`, the sync time,
  or "unknown"?
- Article content: store the feed's HTML as-is and sanitise at render time, or extract text at sync
  time?

## Reproducing the probe

The probes were throwaway (kept outside the repository) and lived side by side: `rssparser-probe`
(approach A) and `ktor-probe` (approach B) in one KMP build, with the four samples above. Approach A
is a `commonMain` object calling `RssParser().parse(xml)` plus a `jvmMain` CLI that prints
`channel.title`, `items.size` and the first item's `title`/`link`/`pubDate`/`rawEnclosure` per sample
— ~45 lines of shared code plus ~65 lines of CLI. Approach B adds `@Serializable` models decoded with
`XML.v1 { repairNamespaces = true; setIndent(0); policy { ignoreUnknownChildren() } }` behind a Ktor
`HttpClient(OkHttp)` CLI; the whole non-product cost of this evaluation is those two modules and the
samples.
