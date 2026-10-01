package dev.appoutlet.outpost.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.materialkolor.rememberDynamicColorScheme
import dev.appoutlet.outpost.core.config.OutpostConfig

@Composable
fun OutpostTheme(
    theme: OutpostConfig.Theme,
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = rememberDynamicColorScheme(
        seedColor = theme.seedColor,
        isDark = isDark,
        style = theme.style,
        contrastLevel = theme.contractLevel.value,
    )

    MaterialTheme(colorScheme = colorScheme, content = content)
}
