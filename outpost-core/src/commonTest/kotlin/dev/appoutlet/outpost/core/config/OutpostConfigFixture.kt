package dev.appoutlet.outpost.core.config

fun OutpostConfig.Companion.fixture(
    theme: OutpostConfig.Theme = OutpostConfig.Theme.fixture(),
    appMetadata: AppMetadata = AppMetadata.fixture(),
    content: List<Content> = listOf(Articles.fixture()),
) = OutpostConfig(
    theme = theme,
    appMetadata = appMetadata,
    content = content,
)
