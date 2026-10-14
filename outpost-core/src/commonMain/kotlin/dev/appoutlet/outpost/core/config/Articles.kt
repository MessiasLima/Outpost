package dev.appoutlet.outpost.core.config

data class Articles(
    val sources: List<ArticleSource>,
) : Content()

sealed interface ArticleSource


fun OutpostConfigContentBuilder.articles(setup: ArticlesContentBuilder.() -> Unit = {}) {
    add(ArticlesContentBuilder().apply(setup).build())
}

@OutpostConfigDsl
class ArticlesContentBuilder {
    val feeds = mutableListOf<ArticleSource>()

    fun rss(url: String) {
        feeds.add(RssFeed(url = url))
    }

    fun build(): Articles {
        return Articles(sources = feeds.toList())
    }
}