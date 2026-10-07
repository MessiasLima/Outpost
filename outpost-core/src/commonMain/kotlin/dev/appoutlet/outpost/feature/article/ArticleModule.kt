package dev.appoutlet.outpost.feature.article

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val articleModule = module {
    navigation<ArticleNavKey> {
        ArticleScreen()
    }
}

@Serializable
object ArticleNavKey : NavKey
