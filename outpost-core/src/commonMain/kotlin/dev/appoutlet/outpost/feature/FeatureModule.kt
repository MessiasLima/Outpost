package dev.appoutlet.outpost.feature

import dev.appoutlet.outpost.feature.article.articleModule
import dev.appoutlet.outpost.feature.articlelist.articleListModule
import dev.appoutlet.outpost.feature.home.homeModule
import dev.appoutlet.outpost.feature.main.mainModule
import org.koin.dsl.module

val featureModule = module {
    includes(
        articleModule,
        articleListModule,
        homeModule,
        mainModule
    )
}
