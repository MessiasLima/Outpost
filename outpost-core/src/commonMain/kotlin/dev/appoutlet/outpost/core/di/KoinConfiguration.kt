package dev.appoutlet.outpost.core.di

import dev.appoutlet.outpost.appModule
import dev.appoutlet.outpost.core.config.OutpostConfig
import dev.appoutlet.outpost.core.logging.getKoinLogger
import org.koin.dsl.koinConfiguration

fun outpostKoinConfiguration(outpostConfig: OutpostConfig) = koinConfiguration {
    logger(getKoinLogger())
    modules(appModule(outpostConfig))
}
