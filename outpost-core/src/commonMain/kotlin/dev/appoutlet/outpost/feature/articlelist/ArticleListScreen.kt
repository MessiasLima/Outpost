package dev.appoutlet.outpost.feature.articlelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.appoutlet.outpost.core.navigation.LocalNavigator
import dev.appoutlet.outpost.feature.article.ArticleNavKey
import kotlin.uuid.Uuid

@Composable
fun ArticleListScreen() {
    val navigator = LocalNavigator.current
    Column(modifier = Modifier.fillMaxSize(),) {
        Button(onClick = { navigator.back() }) {
            Text("Back")
        }
        Text("Article List Screen")
        Button(onClick = {
            navigator.navigate(ArticleNavKey(Uuid.random().toString()))
        }) { Text("Go to Article") }
    }
}
