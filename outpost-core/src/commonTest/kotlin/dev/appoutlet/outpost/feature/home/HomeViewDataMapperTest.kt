package dev.appoutlet.outpost.feature.home

import dev.appoutlet.outpost.core.config.*
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class HomeViewDataMapperTest {
    private val fixtureOutpostConfig = OutpostConfig.fixture()
    private val subject = HomeViewDataMapper(fixtureOutpostConfig)

    @Test
    fun `should map config metadata and content to view data`() {
        val result = subject.map()

        result.header.title shouldBe fixtureOutpostConfig.appMetadata.title
        result.header.headline shouldBe fixtureOutpostConfig.appMetadata.headline
        result.sections shouldBe listOf(
            HomeViewData.Section.Articles,
            HomeViewData.Section.Videos,
            HomeViewData.Section.Podcasts,
        )
    }
}
