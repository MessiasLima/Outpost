package dev.appoutlet.outpost.{{PKG}}.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.appoutlet.outpost.{{PKG}}.{{SYMBOL}}App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            {{SYMBOL}}App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    {{SYMBOL}}App()
}
