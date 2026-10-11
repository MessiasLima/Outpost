package dev.appoutlet.outpost.feature.article

import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import dev.appoutlet.outpost.core.navigation.ScreenType
import kotlinx.serialization.Serializable
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val articleModule = module {
    navigation<ArticleNavKey>(
        metadata = ListDetailSceneStrategy.extraPane() + ListDetailSceneStrategy.preferredPaneSize(
            .66f
        )
    ) { article ->
        ArticleScreenCommon(id = article.id)
    }

    navigation<FeaturedArticleNavKey>(
        metadata = ListDetailSceneStrategy.detailPane() + ListDetailSceneStrategy.preferredPaneSize(
            .33f
        )
    ) { article ->
        ArticleScreenCommon(id = article.id)
    }
}

@Suppress("UnusedPrivateProperty")
@Composable
private fun ArticleScreenCommon(id: String) {
    ArticleScreen(id)
}

@Serializable
data class ArticleNavKey(val id: String) : NavKey, ScreenType.Media

@Serializable
data class FeaturedArticleNavKey(val id: String) : NavKey, ScreenType.Media
