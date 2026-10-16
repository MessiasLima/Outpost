package dev.appoutlet.outpost.core.config

sealed class Content

data class RssFeed(val url: String) : ArticleSource, PodcastSource {
    companion object
}
