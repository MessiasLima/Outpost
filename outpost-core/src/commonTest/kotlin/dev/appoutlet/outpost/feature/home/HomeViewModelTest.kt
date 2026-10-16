package dev.appoutlet.outpost.feature.home

import dev.appoutlet.outpost.core.testing.ViewModelTest
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.kotest.matchers.shouldBe
import kotlin.test.Test

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
