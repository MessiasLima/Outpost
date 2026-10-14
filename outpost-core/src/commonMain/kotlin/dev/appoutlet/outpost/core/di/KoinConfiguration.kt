package dev.appoutlet.outpost.core.di

import dev.appoutlet.outpost.appModule
import dev.appoutlet.outpost.core.config.OutpostConfig
import org.koin.dsl.koinConfiguration

fun outpostKoinConfiguration(outpostConfig: OutpostConfig) = koinConfiguration {
    modules(appModule(outpostConfig))
}
