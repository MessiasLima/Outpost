package dev.appoutlet.outpost.feature.home

import androidx.lifecycle.ViewModel
import kotlinx.serialization.Serializable
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class HomeViewModel(
    private val homeViewDataMapper: HomeViewDataMapper,
) : ViewModel(), OrbitContainerHost<HomeState, HomeState, HomeSideEffect> {
    override val container = orbitContainer(HomeState.Idle) {
        reduce { HomeState.Success(viewData = homeViewDataMapper.map()) }
    }
}

@Serializable
sealed interface HomeState {
    data object Idle : HomeState
    data class Success(val viewData: HomeViewData) : HomeState
}

data class HomeViewData(val sections: List<Section>) {
    sealed interface Section {
        data object Articles : Section
        data object Videos : Section
        data object Podcasts : Section
    }
}

sealed interface HomeSideEffect
