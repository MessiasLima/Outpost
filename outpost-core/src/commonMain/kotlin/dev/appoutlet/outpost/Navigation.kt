package dev.appoutlet.outpost

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import dev.appoutlet.outpost.feature.main.MainNavKey
import org.koin.compose.navigation3.koinEntryProvider

@Composable
internal fun Navigation() {
    val backStack = remember { mutableStateListOf<NavKey>(MainNavKey) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = koinEntryProvider()
    )
}
