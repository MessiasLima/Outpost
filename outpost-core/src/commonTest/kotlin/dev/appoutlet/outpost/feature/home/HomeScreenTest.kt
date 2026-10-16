package dev.appoutlet.outpost.feature.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test

class HomeScreenTest {
    @Test
    fun `should load idle state`() = runComposeUiTest {
        setContent {
            HomeScreen(HomeState.Idle)
        }

        onNodeWithTag("HomeScreen:LoadingIndicator").assertIsDisplayed()
    }
}