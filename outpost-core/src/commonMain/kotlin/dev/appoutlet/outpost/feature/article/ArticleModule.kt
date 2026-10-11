package dev.appoutlet.outpost.feature.article

import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val articleModule = module {
    navigation<ArticleNavKey> { article ->
        ArticleScreen(id = article.id)
    }
}

@Serializable
data class ArticleNavKey(val id: String) : NavKey
