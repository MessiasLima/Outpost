package dev.appoutlet.outpost

import dev.appoutlet.outpost.core.coreModule
import dev.appoutlet.outpost.data.dataModule
import dev.appoutlet.outpost.feature.featureModule
import org.koin.dsl.module

val appModule = module {
    includes(coreModule)
    includes(dataModule)
    includes(featureModule)
}
