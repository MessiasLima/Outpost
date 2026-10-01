package dev.appoutlet.outpost

import androidx.compose.runtime.Composable
import dev.appoutlet.outpost.core.config.OutpostConfig
import dev.appoutlet.outpost.core.config.outpostConfig
import dev.appoutlet.outpost.core.ui.theme.OutpostTheme

@Composable
fun App(config: OutpostConfig = outpostConfig()) {
    OutpostTheme(theme = config.theme) {
        Navigation()
    }
}
