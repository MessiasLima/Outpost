package dev.appoutlet.outpost.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(state: HomeState) {
    AnimatedContent(targetState = state, contentKey = { it::class }) { homeState ->
        when (homeState) {
            HomeState.Idle -> {}
            is HomeState.Success -> HomeScreenSuccess(homeState.viewData)
        }
    }
}

@Composable
private fun HomeScreenSuccess(viewData: HomeViewData) {
    LazyColumn {
        items(viewData.sections) { section ->
            when (section) {
                HomeViewData.Section.Articles -> ArticlesSection()
                HomeViewData.Section.Podcasts -> PodcastsSection()
                HomeViewData.Section.Videos -> VideosSection()
            }
        }
    }
}

@Composable
private fun ArticlesSection() {
    Text("Articles")
}

@Composable
private fun PodcastsSection() {
    Text("Podcasts")
}

@Composable
private fun VideosSection() {
    Text("Videos")
}
