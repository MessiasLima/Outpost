package dev.appoutlet.outpost.core.config

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class OutpostConfigTest {
    @Test
    fun `default config has empty content`() {
        val config = outpostConfig()

        assertTrue(config.content.isEmpty())
    }

    @Test
    fun `content types are preserved in declaration order`() {
        val config = outpostConfig {
            content {
                articles { rss("https://blog1.com/rss.xml") }
                videos { youtubeChannel("channel-id") }
                podcasts { rss("https://podcast.com/rss.xml") }
            }
        }

        assertEquals(3, config.content.size)
        assertIs<Articles>(config.content[0])
        assertIs<Videos>(config.content[1])
        assertIs<Podcasts>(config.content[2])
    }

    @Test
    fun `articles collects rss feeds`() {
        val config = outpostConfig {
            content {
                articles {
                    rss("https://blog1.com/rss.xml")
                    rss("https://blog2.com/rss.xml")
                }
            }
        }

        val articles = config.content.single()
        assertIs<Articles>(articles)
        assertEquals(
            listOf(
                RssFeed("https://blog1.com/rss.xml"),
                RssFeed("https://blog2.com/rss.xml"),
            ),
            articles.sources
        )
    }

    @Test
    fun `videos collects youtube channels`() {
        val config = outpostConfig {
            content {
                videos {
                    youtubeChannel("channel-id-1")
                    youtubeChannel("channel-id-2")
                }
            }
        }

        val videos = config.content.single()
        assertIs<Videos>(videos)
        assertEquals(
            listOf(
                VideoSource.YouTubeChannel("channel-id-1"),
                VideoSource.YouTubeChannel("channel-id-2"),
            ),
            videos.sources
        )
    }

    @Test
    fun `podcasts collects rss feeds`() {
        val config = outpostConfig {
            content {
                podcasts {
                    rss("https://podcast1.com/rss.xml")
                    rss("https://podcast2.com/rss.xml")
                }
            }
        }

        val podcasts = config.content.single()
        assertIs<Podcasts>(podcasts)
        assertEquals(
            listOf(
                RssFeed("https://podcast1.com/rss.xml"),
                RssFeed("https://podcast2.com/rss.xml"),
            ),
            podcasts.sources
        )
    }
}
