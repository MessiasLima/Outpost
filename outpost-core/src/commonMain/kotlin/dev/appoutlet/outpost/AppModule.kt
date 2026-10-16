package dev.appoutlet.outpost

import dev.appoutlet.outpost.core.config.OutpostConfig
import dev.appoutlet.outpost.core.coreModule
import dev.appoutlet.outpost.data.dataModule
import dev.appoutlet.outpost.feature.featureModule
import org.koin.dsl.module

fun appModule(outpostConfig: OutpostConfig) = module {
    single { outpostConfig }
    includes(coreModule)
    includes(dataModule)
    includes(featureModule)
}
