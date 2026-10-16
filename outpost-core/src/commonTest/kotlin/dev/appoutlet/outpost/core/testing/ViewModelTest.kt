package dev.appoutlet.outpost.core.testing

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.test.OrbitScopedTestContextInternal
import org.orbitmvi.orbit.test.testWithInternalState
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * Base class for ViewModel tests.
 *
 * It installs a test main dispatcher, which is required because an Orbit container is created with
 * the ViewModel scope, then creates the ViewModel under test through [createViewModel].
 *
 * Use [test] to exercise the container: it runs the `onCreate` block and the validation block
 * inside a single coroutine test.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class ViewModelTest<VM, State : Any, SideEffect : Any>
    where VM : ViewModel, VM : OrbitContainerHost<State, State, SideEffect> {
    protected lateinit var viewModel: VM

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = createViewModel()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    protected abstract fun createViewModel(): VM

    protected fun assert(
        validate: suspend OrbitScopedTestContextInternal<State, State, SideEffect, VM>.() -> Unit,
    ) = runTest {
        viewModel.testWithInternalState(this) {
            runOnCreate()
            validate()
            cancelAndIgnoreRemainingItems()
        }
    }
}
