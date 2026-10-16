package dev.appoutlet.outpost

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.appoutlet.outpost.core.config.app
import dev.appoutlet.outpost.core.config.outpostConfig
import kotlin.test.Test

class AppTest {
    @Test
    fun `should load navigation`() = runComposeUiTest {
        setContent { App() }

        onNodeWithTag("Navigation")
            .assertExists()
    }
}
