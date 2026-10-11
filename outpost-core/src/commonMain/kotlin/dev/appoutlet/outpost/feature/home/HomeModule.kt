package dev.appoutlet.outpost.feature.home

import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val homeModule = module {
    navigation<HomeNavKey>(
        metadata = ListDetailSceneStrategy.listPane() +
            ListDetailSceneStrategy.preferredPaneSize(.33f)
    ) {
        HomeScreen()
    }
}

@Serializable
object HomeNavKey : NavKey
