package dev.appoutlet.outpost

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import dev.appoutlet.outpost.core.navigation.LocalNavigator
import dev.appoutlet.outpost.core.navigation.Navigator
import dev.appoutlet.outpost.feature.home.HomeNavKey
import org.koin.compose.navigation3.koinEntryProvider

@Composable
internal fun Navigation() {
    val navigator = remember { Navigator(HomeNavKey) }

    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()

    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
    }

    val listDetailSceneStrategy = rememberListDetailSceneStrategy<NavKey>(
        directive = directive,
    )

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavDisplay(
            backStack = navigator.backStack,
            onBack = { navigator.back() },
            sceneStrategies = listOf(listDetailSceneStrategy),
            entryProvider = koinEntryProvider()
        )
    }
}
