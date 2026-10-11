package dev.appoutlet.outpost.feature.articlelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.appoutlet.outpost.core.navigation.LocalNavigator
import dev.appoutlet.outpost.feature.article.ArticleNavKey
import kotlin.uuid.Uuid

@Composable
fun ArticleListScreen() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue),
    ) {
        Button(onClick = { navigator.back() }) {
            Text("Back")
        }
        Text("Article List Screen")
        Button(onClick = {
            navigator.navigate(ArticleNavKey(Uuid.random().toString()))
        }) {
            Text("Go to Article")
        }
    }
}
