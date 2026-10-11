package dev.appoutlet.outpost.feature.articlelist

import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val articleListModule = module {
    navigation<ArticleListNavKey>(
        metadata = ListDetailSceneStrategy.detailPane() + ListDetailSceneStrategy.preferredPaneSize(width = .33f)
    ) {
        ArticleListScreen()
    }
}

@Serializable
object ArticleListNavKey : NavKey
