# Base Unit Test Pattern

Use this shape for repository tests and other unit tests that mock collaborators.

```kotlin
private val mockTaskDao = mock<TaskDao>()
private val mockTimeProvider = mock<TimeProvider>()
private val subject = TaskRepositoryImpl(mockTaskDao, mockTimeProvider)

@Test
fun `should return tasks due today and overdue tasks`() = runTest {
    val endOfToday = Instant.parse("2026-07-22T23:59:59.999999999Z")
    val fixtureTasks = listOf(Task.fixture())

    every { mockTimeProvider.endOfToday() } returns endOfToday
    every { mockTaskDao.findTodayTasks(endOfToday) } returns flowOf(fixtureTasks)

    subject.findTodayTasks().first() shouldBe fixtureTasks
}
```

Use this shape for mapper tests and other unit tests that transform a fixture into a structured result.

```kotlin
private val fixtureOutpostConfig = OutpostConfig.fixture()
private val subject = HomeViewDataMapper(fixtureOutpostConfig)

@Test
fun `should map config metadata and content to view data`() {
    val result = subject.map()

    result.header.title shouldBe fixtureOutpostConfig.appMetadata.title
    result.header.headline shouldBe fixtureOutpostConfig.appMetadata.headline
    result.sections shouldBe listOf(
        HomeViewData.Section.Articles,
        HomeViewData.Section.Videos,
        HomeViewData.Section.Podcasts,
    )
}
```

## Notes

1. Create the `subject` at the class level so every test exercises the same production instance.
2. Use Mokkery for collaborators.
3. Prefer fixtures for returned objects and inputs.
4. For `Flow`-based APIs, stub with `flowOf(...)` and assert with `.first()`.
5. Keep the test focused on the unit boundary instead of persistence details.
6. When the result contains multiple fields, assert each field individually against the fixture/input values. This makes the failing value obvious instead of reporting a generic whole-object mismatch.
