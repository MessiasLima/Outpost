package dev.appoutlet.outpost.feature.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.appoutlet.outpost.core.navigation.LocalNavigator

@Composable
fun ArticleScreen(id: String) {
    val navigator = LocalNavigator.current

    Column(modifier = Modifier.fillMaxSize(),) {
        Button(onClick = { navigator.back() }) { Text("Back") }
        Text("ArticleScreen: $id")
    }
}
