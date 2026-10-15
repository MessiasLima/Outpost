package dev.appoutlet.outpost.core.config

@OutpostConfigDsl
class OutpostConfigBuilder {
    var theme = OutpostConfig.Theme.Default
    lateinit var appMetadata: AppMetadata
    var content: List<Content> = emptyList()

    fun theme(themeSetup: OutpostConfigThemeBuilder.() -> Unit) {
        theme = OutpostConfigThemeBuilder().apply(themeSetup).build()
    }

    fun content(contentSetup: OutpostConfigContentBuilder.() -> Unit) {
        content = OutpostConfigContentBuilder().apply(contentSetup).build()
    }

    fun build(): OutpostConfig {
        return OutpostConfig(
            theme = theme,
            appMetadata = appMetadata,
            content = content
        )
    }
}

@OutpostConfigDsl
class OutpostConfigThemeBuilder {
    var seedColor = OutpostConfig.Theme.Default.seedColor
    var style = OutpostConfig.Theme.Default.style
    var contractLevel = OutpostConfig.Theme.Default.contractLevel
    fun build() = OutpostConfig.Theme(seedColor, style, contractLevel)
}

fun outpostConfig(setup: OutpostConfigBuilder.() -> Unit = {}): OutpostConfig {
    val outpostConfigBuilder = OutpostConfigBuilder()
    outpostConfigBuilder.setup()
    return outpostConfigBuilder.build()
}
