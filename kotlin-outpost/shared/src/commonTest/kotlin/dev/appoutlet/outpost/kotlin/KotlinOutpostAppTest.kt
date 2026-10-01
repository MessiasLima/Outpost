package dev.appoutlet.outpost.kotlin

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test

class KotlinOutpostAppTest {
    @Test
    fun `should load app`() = runComposeUiTest {
        setContent {
            KotlinOutpostApp()
        }

        onNodeWithTag("Navigation")
            .isDisplayed()
    }
}
