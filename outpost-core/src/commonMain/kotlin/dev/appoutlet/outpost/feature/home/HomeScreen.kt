package dev.appoutlet.outpost.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.User
import dev.appoutlet.outpost.core.ui.modifier.widthInCompact
import dev.appoutlet.outpost.generated.resources.Res
import dev.appoutlet.outpost.generated.resources.home_articles
import dev.appoutlet.outpost.generated.resources.home_podcasts
import dev.appoutlet.outpost.generated.resources.home_profile_a11y
import dev.appoutlet.outpost.generated.resources.home_search_text
import dev.appoutlet.outpost.generated.resources.home_search_title
import dev.appoutlet.outpost.generated.resources.home_see_more
import dev.appoutlet.outpost.generated.resources.home_videos
import org.jetbrains.compose.resources.stringResource

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
        item { HomeScreenHeader(viewData.header) }
        items(viewData.sections) { section ->
            when (section) {
                HomeViewData.Section.Articles -> ArticlesSection()
                HomeViewData.Section.Podcasts -> PodcastsSection()
                HomeViewData.Section.Videos -> VideosSection()
            }
        }
        item { SearchPanel() }
    }
}

@Composable
private fun HomeScreenHeader(header: HomeViewData.Header) {
    val topPadding = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
    Row(
        modifier = Modifier.padding(
            top = topPadding + 8.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 8.dp,
        ).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = header.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(text = header.headline)
        }

        FilledTonalIconButton(onClick = {}) {
            Icon(
                imageVector = Lucide.User,
                contentDescription = stringResource(Res.string.home_profile_a11y)
            )
        }
    }
}

@Composable
private fun ArticlesSection() {
    Section(title = stringResource(Res.string.home_articles), onSeeMoreClick = {}) {}
}

@Composable
private fun PodcastsSection() {
    Section(title = stringResource(Res.string.home_podcasts), onSeeMoreClick = {}) {}
}

@Composable
private fun VideosSection() {
    Section(title = stringResource(Res.string.home_videos), onSeeMoreClick = {}) {}
}

@Composable
private fun Section(title: String, onSeeMoreClick: () -> Unit, content: @Composable () -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable(onClick = onSeeMoreClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )

            TextButton(onClick = onSeeMoreClick) {
                Text(text = stringResource(Res.string.home_see_more))
            }
        }

        content()
    }
}

@Composable
private fun SearchPanel() {
    val bottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding() + 8.dp
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.widthInCompact().padding(
                top = 8.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = bottomPadding
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(Res.string.home_search_title),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(Res.string.home_search_text),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
