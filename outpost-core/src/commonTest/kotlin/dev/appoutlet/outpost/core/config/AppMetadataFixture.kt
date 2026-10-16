package dev.appoutlet.outpost.core.config

fun AppMetadata.Companion.fixture(
    title: String = "Outpost",
    headline: String = "Your personal content hub",
) = AppMetadata(
    title = title,
    headline = headline,
)
