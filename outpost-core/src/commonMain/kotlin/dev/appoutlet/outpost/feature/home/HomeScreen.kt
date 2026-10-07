package dev.appoutlet.outpost.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.appoutlet.outpost.core.navigation.LocalNavigator
import dev.appoutlet.outpost.feature.articlelist.ArticleListNavKey

@Composable
fun HomeScreen() {
    val navigator = LocalNavigator.current

    Column {
        Text("HomeScreen")
        Button(onClick = {
            navigator.navigate(ArticleListNavKey)
        }) {
            Text("Go to articles")
        }
    }
}
