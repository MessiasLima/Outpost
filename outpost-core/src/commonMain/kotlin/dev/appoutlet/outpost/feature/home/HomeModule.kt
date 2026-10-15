package dev.appoutlet.outpost.feature.home

import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.koin.plugin.module.dsl.factory
import org.koin.plugin.module.dsl.viewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

val homeModule = module {
    viewModel<HomeViewModel>()
    factory<HomeViewDataMapper>()
    navigation<HomeNavKey>(
        metadata = ListDetailSceneStrategy.listPane() +
            ListDetailSceneStrategy.preferredPaneSize(.33f)
    ) {
        val viewModel = koinViewModel<HomeViewModel>()
        val state by viewModel.collectAsState()

        HomeScreen(state)

        viewModel.collectSideEffect { sideEffect ->
        }
    }
}

@Serializable
object HomeNavKey : NavKey
