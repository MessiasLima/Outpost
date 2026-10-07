package dev.appoutlet.outpost.core.di

import dev.appoutlet.outpost.appModule
import org.koin.dsl.koinConfiguration

val outpostKoinConfiguration = koinConfiguration {
    modules(appModule)
}
