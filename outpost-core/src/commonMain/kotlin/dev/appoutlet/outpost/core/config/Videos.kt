package dev.appoutlet.outpost.core.config

data class Videos(
    val sources: List<VideoSource>,
) : Content() {
    companion object
}

sealed interface VideoSource {
    data class YouTubeChannel(
        val id: String,
    ) : VideoSource {
        companion object
    }
}

fun OutpostConfigContentBuilder.videos(setup: VideosContentBuilder.() -> Unit = {}) {
    add(VideosContentBuilder().apply(setup).build())
}

@OutpostConfigDsl
class VideosContentBuilder {
    val sources = mutableListOf<VideoSource>()

    fun youtubeChannel(id: String) {
        sources.add(VideoSource.YouTubeChannel(id = id))
    }

    fun build(): Videos {
        return Videos(
            sources = sources.toList()
        )
    }
}
