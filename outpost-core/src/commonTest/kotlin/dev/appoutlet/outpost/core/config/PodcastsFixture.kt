package dev.appoutlet.outpost.core.config

fun Podcasts.Companion.fixture(
    sources: List<PodcastSource> = listOf(RssFeed.fixture()),
) = Podcasts(
    sources = sources,
)
