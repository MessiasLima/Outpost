package dev.appoutlet.outpost.feature.articlelist

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val articleListModule = module {
    navigation<ArticleListNavKey> {
        ArticleListScreen()
    }
}

@Serializable
object ArticleListNavKey : NavKey
