package dev.appoutlet.outpost.kotlin

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import dev.appoutlet.outpost.App
import dev.appoutlet.outpost.core.config.articles
import dev.appoutlet.outpost.core.config.outpostConfig
import dev.appoutlet.outpost.core.config.podcasts
import dev.appoutlet.outpost.core.config.videos

private val KotlinColor = Color(0xFF6B57FF)

private val KotlinOutpostConfig = outpostConfig {
    theme {
        seedColor = KotlinColor
        style = PaletteStyle.Fidelity
    }
    content {
        articles {
            rss("https://blog.jetbrains.com/kotlin/feed/")
        }

        videos {
            youtubeChannel("UC5DwS2-lzqd3Vcv9Ai85kgQ" )
        }

        podcasts {
            rss("https://feeds.megaphone.fm/JNPD1328829962.xml")
        }
    }
}

@Composable
fun KotlinOutpostApp() {
    App(config = KotlinOutpostConfig)
}
