package dev.appoutlet.outpost.core.navigation

import androidx.compose.runtime.staticCompositionLocalOf

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("Navigator not provided")
}