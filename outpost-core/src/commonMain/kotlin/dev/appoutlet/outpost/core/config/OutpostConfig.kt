package dev.appoutlet.outpost.core.config

import androidx.compose.ui.graphics.Color
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle

data class OutpostConfig(
    val theme: Theme,
) {
    data class Theme(
        val seedColor: Color,
        val style: PaletteStyle,
        val contractLevel: Contrast
    ) {
        companion object {
            val Default = Theme(
                seedColor = Color.Magenta,
                style = PaletteStyle.TonalSpot,
                contractLevel = Contrast.Default
            )
        }
    }
}

@DslMarker
annotation class OutpostConfigDsl

@OutpostConfigDsl
class OutpostConfigBuilder {
    private var theme = OutpostConfig.Theme.Default

    fun theme(themeSetup: OutpostConfigThemeBuilder.() -> Unit) {
        theme = OutpostConfigThemeBuilder().apply(themeSetup).build()
    }

    fun build(): OutpostConfig {
        return OutpostConfig(
            theme = theme
        )
    }
}

@OutpostConfigDsl
class OutpostConfigThemeBuilder {
    var seedColor = OutpostConfig.Theme.Default.seedColor
    var style = OutpostConfig.Theme.Default.style
    var contractLevel = OutpostConfig.Theme.Default.contractLevel
    fun build() = OutpostConfig.Theme(seedColor, style, contractLevel)
}

fun outpostConfig(setup: OutpostConfigBuilder.() -> Unit): OutpostConfig {
    val outpostConfigBuilder = OutpostConfigBuilder()
    outpostConfigBuilder.setup()
    return outpostConfigBuilder.build()
}
