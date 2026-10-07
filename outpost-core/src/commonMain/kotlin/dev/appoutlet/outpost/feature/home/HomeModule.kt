package dev.appoutlet.outpost.feature.home

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val homeModule = module {
    navigation<HomeNavKey> {
        HomeScreen()
    }
}

@Serializable
object HomeNavKey : NavKey
