package dev.appoutlet.outpost.kotlin.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.appoutlet.outpost.kotlin.KotlinOutpostApp
import org.jetbrains.compose.resources.painterResource
import outpost.kotlin_outpost.desktop.generated.resources.Res
import outpost.kotlin_outpost.desktop.generated.resources.icon

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kotlin Outpost",
        icon = painterResource(Res.drawable.icon)
    ) {
        KotlinOutpostApp()
    }
}
