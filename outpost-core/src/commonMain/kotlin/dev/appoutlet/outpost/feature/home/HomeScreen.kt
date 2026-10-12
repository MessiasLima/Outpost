package dev.appoutlet.outpost.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.appoutlet.outpost.core.navigation.LocalNavigator
import dev.appoutlet.outpost.feature.article.FeaturedArticleNavKey
import dev.appoutlet.outpost.feature.articlelist.ArticleListNavKey

@Composable
fun HomeScreen() {
    val navigator = LocalNavigator.current

    Column(modifier = Modifier.fillMaxSize()) {
        Text("HomeScreen")
        Button(onClick = { navigator.navigate(ArticleListNavKey) }) {
            Text("Go to articles")
        }
        Button(onClick = {
            navigator.navigate(FeaturedArticleNavKey("featured"))
        }) { Text("Go to featured article") }
    }
}
