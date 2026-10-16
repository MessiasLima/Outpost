package dev.appoutlet.outpost.{{PKG}}

import androidx.compose.runtime.Composable
import dev.appoutlet.outpost.App
import dev.appoutlet.outpost.core.config.app
import dev.appoutlet.outpost.core.config.outpostConfig
import org.jetbrains.compose.resources.stringResource
import {{RES_PKG}}.Res
import {{RES_PKG}}.app_headline
import {{RES_PKG}}.app_name

@Composable
fun {{SYMBOL}}App() {
    val appTitle = stringResource(Res.string.app_name)
    val appHeadline = stringResource(Res.string.app_headline)

    App(
        config = outpostConfig {
            app {
                title = appTitle
                headline = appHeadline
            }

            // Theme and content are brand decisions, so the variant starts on the Outpost
            // defaults. Declare them here once the variant has its own palette and sources:
            //   theme { seedColor = Color(0xFF6B57FF) }
            //   content { articles { rss("https://example.com/feed") } }
        }
    )
}
