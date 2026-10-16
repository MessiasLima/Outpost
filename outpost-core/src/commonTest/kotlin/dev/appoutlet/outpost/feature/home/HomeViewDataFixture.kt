package dev.appoutlet.outpost.feature.home

fun HomeViewData.Companion.fixture(
    header: HomeViewData.Header = HomeViewData.Header.fixture(),
    sections: List<HomeViewData.Section> = listOf(
        HomeViewData.Section.Articles,
        HomeViewData.Section.Videos,
        HomeViewData.Section.Podcasts,
    ),
) = HomeViewData(
    header = header,
    sections = sections,
)

fun HomeViewData.Header.Companion.fixture(
    title: String = "Outpost",
    headline: String = "Your personal content hub",
) = HomeViewData.Header(
    title = title,
    headline = headline,
)
