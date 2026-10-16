# ViewModel Unit Test Pattern

Use this pattern for ViewModel tests in `commonTest`. ViewModel tests extend the shared
`dev.appoutlet.outpost.core.testing.ViewModelTest` base class, which manages the main dispatcher
and runs the Orbit container in a coroutine test.

```kotlin
class HomeViewModelTest : ViewModelTest<HomeViewModel, HomeState, HomeSideEffect>() {
    private val mockHomeViewDataMapper = mock<HomeViewDataMapper>()

    override fun createViewModel() = HomeViewModel(mockHomeViewDataMapper)

    @Test
    fun `should reduce to success state with mapped view data on create`() {
        val viewDataFixture = HomeViewData.fixture()
        every { mockHomeViewDataMapper.map() } returns viewDataFixture

        viewModel.container.stateFlow.value shouldBe HomeState.Idle
        assert {
            expectInternalState(HomeState.Success(viewDataFixture))
        }
    }
}
```

## Rules

1. MUST put ViewModel unit tests in the module's `commonTest` source set.
2. MUST extend the shared `ViewModelTest<VM, State, SideEffect>` base and implement
   `createViewModel()`; do not set or reset `Dispatchers.Main`, create `runTest`, or configure the
   Orbit test container in each test class.
3. MUST create collaborators as Mokkery mocks and configure their answers before calling the base
   class's `assert { ... }` helper. The helper starts the Orbit test and runs `onCreate`, so any
   collaborator used during initialization must already be stubbed.
4. MUST reuse fixtures for view data and other fixture-capable values. Override fixture defaults
   only when the test requires a specific value.
5. MUST use `viewModel.container.stateFlow.value shouldBe ...` to assert the synchronous initial
   state when that is part of the behavior being tested.
6. MUST use Orbit test-context assertions such as `expectInternalState(...)` and
   `expectSideEffect(...)` inside `assert { ... }` to check states and side effects produced by
   `onCreate` or ViewModel actions.
7. MUST NOT expect the initial state again with `expectInternalState` inside `assert { ... }` by
   default. Orbit's test framework automatically checks and consumes the initial state before the
   block runs; expecting it again waits for an emission that will not occur. Assert the initial
   value directly through `stateFlow.value`, or change the shared test setup only when a test
   specifically needs to disable Orbit's automatic initial-state check.
8. MUST NOT wrap a test in another `runTest`; the base class's `assert { ... }` helper already
   creates the coroutine test scope and runs `onCreate`.
9. MUST use Kotest assertions such as `shouldBe` for direct assertions. The base class helper named
   `assert { ... }` is the Orbit test wrapper, not Kotlin's plain `assert()` function.
10. MUST verify collaborator calls only when those calls are meaningful behavior; use Mokkery
    verification after the Orbit test block when needed.
11. MUST name test methods with backticks and sentence-style names, and combine related state or
    side-effect assertions from the same behavior in one test.

## Notes

- The base class runs `runOnCreate()` for every test and cancels remaining container work when the
  block completes. ViewModels without an `onCreate` intent can use the same pattern.
- `expectInternalState` is used because these ViewModels expose the same internal and external
  state type. If a ViewModel has distinct internal and external types, update the shared base/test
  abstraction to support that shape rather than asserting the wrong state stream.
- ViewModel unit tests exercise ViewModel state and effects. UI rendering belongs in Compose UI
  tests using explicit state/view data, not a real ViewModel.
