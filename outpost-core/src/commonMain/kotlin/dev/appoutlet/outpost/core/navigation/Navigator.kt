package dev.appoutlet.outpost.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey

class Navigator(start: NavKey) {
    val backStack: List<NavKey>
        field = mutableStateListOf(start)

    fun navigate(destination: NavKey) {
        if (destination is MediaPage) {
            while (backStack.size > 1 && backStack.last() is MediaPage) {
                back()
            }
        }

        backStack.add(destination)
    }

    fun back() {
        backStack.removeLastOrNull()
    }
}
