package dev.appoutlet.outpost.{{PKG}}

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test

class {{SYMBOL}}AppTest {
    @Test
    fun `should load app`() = runComposeUiTest {
        setContent {
            {{SYMBOL}}App()
        }

        onNodeWithTag("Navigation")
            .isDisplayed()
    }
}
