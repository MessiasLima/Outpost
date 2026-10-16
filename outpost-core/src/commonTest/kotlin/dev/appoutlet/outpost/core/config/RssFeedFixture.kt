package dev.appoutlet.outpost.core.config

fun RssFeed.Companion.fixture(
    url: String = "https://example.com/rss.xml",
) = RssFeed(
    url = url,
)
