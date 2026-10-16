package dev.appoutlet.outpost.core.config

fun Videos.Companion.fixture(
    sources: List<VideoSource> = listOf(VideoSource.YouTubeChannel.fixture()),
) = Videos(
    sources = sources,
)
