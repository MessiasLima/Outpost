package dev.appoutlet.outpost.core.config

import androidx.compose.ui.graphics.Color
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle

fun OutpostConfig.Theme.Companion.fixture(
    seedColor: Color = Color.Magenta,
    style: PaletteStyle = PaletteStyle.TonalSpot,
    contractLevel: Contrast = Contrast.Default,
) = OutpostConfig.Theme(
    seedColor = seedColor,
    style = style,
    contractLevel = contractLevel,
)
