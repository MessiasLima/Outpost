package dev.appoutlet.outpost

import androidx.compose.runtime.Composable
import dev.appoutlet.outpost.core.config.OutpostConfig
import dev.appoutlet.outpost.core.config.outpostConfig
import dev.appoutlet.outpost.core.di.outpostKoinConfiguration
import dev.appoutlet.outpost.core.ui.theme.OutpostTheme
import org.koin.compose.KoinApplication

@Composable
fun App(config: OutpostConfig = outpostConfig()) {
    KoinApplication(configuration = outpostKoinConfiguration) {
        OutpostTheme(theme = config.theme) {
            Navigation()
        }
    }
}
