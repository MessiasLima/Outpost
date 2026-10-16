package dev.appoutlet.outpost.core.logging

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import kotlin.test.Test

class LoggerTest {
    @Test
    fun `getLogger tags the logger with the given name`() {
        getLogger("Feature").tag shouldBe "Feature"
    }

    @Test
    fun `logger delegate derives the tag from the owning class`() {
        LoggingHost().log.tag shouldBe "LoggingHost"
    }

    @Test
    fun `getKoinLogger returns a koin logger`() {
        getKoinLogger().shouldBeInstanceOf<Logger>()
    }

    @Test
    fun `koin logger forwards messages at its level`() {
        val logger = getKoinLogger()

        logger.log(Level.INFO, "Koin message")

        logger.isAt(Level.INFO) shouldBe true
    }
}

private class LoggingHost {
    val log by logger()
}
