package dev.appoutlet.outpost.kotlin

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import dev.appoutlet.outpost.App
import dev.appoutlet.outpost.core.config.app
import dev.appoutlet.outpost.core.config.articles
import dev.appoutlet.outpost.core.config.outpostConfig
import dev.appoutlet.outpost.core.config.podcasts
import dev.appoutlet.outpost.core.config.videos
import org.jetbrains.compose.resources.stringResource
import outpost.kotlin_outpost.shared.generated.resources.Res
import outpost.kotlin_outpost.shared.generated.resources.app_headline
import outpost.kotlin_outpost.shared.generated.resources.app_name

private val KotlinColor = Color(0xFF6B57FF)

@Composable
fun KotlinOutpostApp() {
    val appTitle = stringResource(Res.string.app_name)
    val appHeadline = stringResource(Res.string.app_headline)

    App(config = outpostConfig {
        theme {
            seedColor = KotlinColor
            style = PaletteStyle.Fidelity
        }

        app {
            title = appTitle
            headline = appHeadline
        }

        content {
            articles {
                rss("https://blog.jetbrains.com/kotlin/feed/")
            }

            videos {
                youtubeChannel("UC5DwS2-lzqd3Vcv9Ai85kgQ")
            }

            podcasts {
                rss("https://feeds.megaphone.fm/JNPD1328829962.xml")
            }
        }
    })
}
