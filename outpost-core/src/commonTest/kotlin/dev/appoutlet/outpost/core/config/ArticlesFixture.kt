package dev.appoutlet.outpost.core.config

fun Articles.Companion.fixture(
    sources: List<ArticleSource> = listOf(RssFeed.fixture()),
) = Articles(
    sources = sources,
)
