package dev.appoutlet.outpost.core.navigation

import dev.appoutlet.outpost.feature.article.ArticleNavKey
import dev.appoutlet.outpost.feature.article.FeaturedArticleNavKey
import dev.appoutlet.outpost.feature.articlelist.ArticleListNavKey
import dev.appoutlet.outpost.feature.home.HomeNavKey
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class NavigatorTest {
    @Test
    fun `starts with the provided destination`() {
        val navigator = Navigator(HomeNavKey)

        navigator.backStack.size shouldBe 1
        navigator.backStack.first() shouldBe HomeNavKey
    }

    @Test
    fun `navigating to a media list appends it to the back stack`() {
        val navigator = Navigator(HomeNavKey)

        navigator.navigate(ArticleListNavKey)

        navigator.backStack.size shouldBe 2
        navigator.backStack shouldBe listOf(HomeNavKey, ArticleListNavKey)
    }

    @Test
    fun `navigating to media replaces the previous media destination`() {
        val navigator = Navigator(HomeNavKey)
        navigator.navigate(ArticleListNavKey)
        navigator.navigate(ArticleNavKey("article-1"))

        val featuredArticle = FeaturedArticleNavKey("featured-article")
        navigator.navigate(featuredArticle)

        navigator.backStack.size shouldBe 3
        navigator.backStack shouldBe listOf(HomeNavKey, ArticleListNavKey, featuredArticle)
    }

    @Test
    fun `back removes the last destination`() {
        val navigator = Navigator(HomeNavKey)
        navigator.navigate(ArticleListNavKey)

        navigator.back()

        navigator.backStack shouldBe listOf(HomeNavKey)
    }

    @Test
    fun `navigating to media does not remove the root destination`() {
        val rootArticle = ArticleNavKey("root-article")
        val navigator = Navigator(rootArticle)
        val featuredArticle = FeaturedArticleNavKey("featured-article")

        navigator.navigate(featuredArticle)

        navigator.backStack shouldBe listOf(rootArticle, featuredArticle)
    }
}
