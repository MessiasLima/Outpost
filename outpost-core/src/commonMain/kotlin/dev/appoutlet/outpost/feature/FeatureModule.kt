package dev.appoutlet.outpost.feature

import dev.appoutlet.outpost.feature.main.mainModule
import org.koin.dsl.module

val featureModule = module {
    includes(
        mainModule
    )
}
