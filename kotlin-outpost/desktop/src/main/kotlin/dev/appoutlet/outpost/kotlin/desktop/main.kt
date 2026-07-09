@file:Suppress("Filename")

package dev.appoutlet.outpost.kotlin.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.appoutlet.outpost.kotlin.KotlinOutpostApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Outpost",
    ) {
        KotlinOutpostApp()
    }
}
