package dev.appoutlet.outpost.{{PKG}}.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.appoutlet.outpost.{{PKG}}.{{SYMBOL}}App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "{{DISPLAY}}",
    ) {
        {{SYMBOL}}App()
    }
}
