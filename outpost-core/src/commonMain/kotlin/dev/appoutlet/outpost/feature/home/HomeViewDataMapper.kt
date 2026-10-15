package dev.appoutlet.outpost.feature.home

import dev.appoutlet.outpost.core.config.Articles
import dev.appoutlet.outpost.core.config.OutpostConfig
import dev.appoutlet.outpost.core.config.Podcasts
import dev.appoutlet.outpost.core.config.Videos

class HomeViewDataMapper(private val config: OutpostConfig) {
    fun map(): HomeViewData {
        val sections = config.content.map { content ->
            when (content) {
                is Articles -> HomeViewData.Section.Articles
                is Podcasts -> HomeViewData.Section.Podcasts
                is Videos -> HomeViewData.Section.Videos
            }
        }

        return HomeViewData(sections)
    }
}