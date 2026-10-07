package dev.appoutlet.outpost.feature.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val mainModule = module {
    navigation<MainNavKey> {
        MainScreen()
    }
}

@Serializable
object MainNavKey : NavKey