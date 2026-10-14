package dev.appoutlet.outpost.core.config

data class Podcasts(
    val sources: List<PodcastSource>,
) : Content()

sealed interface PodcastSource

fun OutpostConfigContentBuilder.podcasts(setup: PodcastsContentBuilder.() -> Unit = {}) {
    add(PodcastsContentBuilder().apply(setup).build())
}

@OutpostConfigDsl
class PodcastsContentBuilder {
    val sources = mutableListOf<PodcastSource>()

    fun rss(url: String) {
        sources.add(RssFeed(url = url))
    }

    fun build(): Podcasts {
        return Podcasts(
            sources = sources.toList()
        )
    }
}
