package dev.appoutlet.outpost.core.config

import androidx.compose.ui.graphics.Color
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle

data class OutpostConfig(
    val theme: Theme,
    val content: List<Content> = emptyList(),
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
