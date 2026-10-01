package dev.appoutlet.outpost.kotlin

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import dev.appoutlet.outpost.App
import dev.appoutlet.outpost.core.config.outpostConfig

private val KotlinColor = Color(0xFF6B57FF)

@Composable
fun KotlinOutpostApp() {
    App(
        outpostConfig {
            theme {
                seedColor = KotlinColor
                style = PaletteStyle.Fidelity
            }
        }
    )
}
