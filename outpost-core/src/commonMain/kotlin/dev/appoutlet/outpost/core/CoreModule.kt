package dev.appoutlet.outpost.core

import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val coreModule = module {
    factory<SampleImplementation>() bind SampleInterface::class
}

internal interface SampleInterface {
    val variable: String
}

class SampleImplementation : SampleInterface {
    override val variable = "Value from injection"
}
