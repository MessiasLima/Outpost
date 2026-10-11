package dev.appoutlet.outpost.feature.article

import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.NavKey
import dev.appoutlet.outpost.core.navigation.MediaPage
import kotlinx.serialization.Serializable
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val articleModule = module {
    navigation<ArticleNavKey>(
        metadata = ListDetailSceneStrategy.extraPane() + ListDetailSceneStrategy.preferredPaneSize(.66f)
    ) { article ->
        ArticleScreen(id = article.id)
    }

    navigation<FeaturedArticleNavKey>(
        metadata = ListDetailSceneStrategy.detailPane()+ ListDetailSceneStrategy.preferredPaneSize(.33f)
    ) { article ->
        ArticleScreen(id = article.id)
    }
}

@Serializable
data class ArticleNavKey(val id: String) : NavKey, MediaPage

@Serializable
data class FeaturedArticleNavKey(val id: String) : NavKey, MediaPage