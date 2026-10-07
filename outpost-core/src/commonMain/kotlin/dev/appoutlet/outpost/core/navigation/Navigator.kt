package dev.appoutlet.outpost.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey

class Navigator(start: NavKey) {
    val backStack: List<NavKey>
        field = mutableStateListOf(start)

    fun navigate(destination: NavKey) {
        backStack.add(destination)
    }

    fun back() {
        backStack.removeLastOrNull()
    }
}