package dev.appoutlet.outpost.feature.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.appoutlet.outpost.generated.resources.Res
import dev.appoutlet.outpost.generated.resources.home_articles
import dev.appoutlet.outpost.generated.resources.home_podcasts
import dev.appoutlet.outpost.generated.resources.home_videos
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class HomeScreenTest {
    @Test
    fun `should load idle state`() = runComposeUiTest {
        setContent {
            HomeScreen(HomeState.Idle)
        }

        onNodeWithTag("HomeScreen:LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun `should load success state`() = runComposeUiTest {
        val viewDataFixture = HomeViewData.fixture()

        setContent {
            HomeScreen(HomeState.Success(viewDataFixture))
        }

        onNodeWithText(viewDataFixture.header.title).assertIsDisplayed()
        onNodeWithText(viewDataFixture.header.headline).assertIsDisplayed()
        onNodeWithTag("HomeScreen:ProfileButton").assertIsDisplayed()
        onNodeWithText(getString(Res.string.home_articles)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.home_videos)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.home_podcasts)).assertIsDisplayed()
        onNodeWithTag("HomeScreen:SearchPanel").assertIsDisplayed()
    }
}
