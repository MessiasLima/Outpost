package dev.appoutlet.outpost

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation3.ui.NavDisplay
import dev.appoutlet.outpost.core.navigation.LocalNavigator
import dev.appoutlet.outpost.core.navigation.Navigator
import dev.appoutlet.outpost.feature.home.HomeNavKey
import org.koin.compose.navigation3.koinEntryProvider

@Composable
internal fun Navigation() {
    val navigator = remember { Navigator(HomeNavKey) }

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavDisplay(
            backStack = navigator.backStack,
            onBack = { navigator.back() },
            entryProvider = koinEntryProvider()
        )
    }
}
