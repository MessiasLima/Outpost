package dev.appoutlet.outpost.core.config

data class AppMetadata(
    val title: String,
    val headline: String,
)

class AppMetadataBuilder {
    var title: String = ""
    var headline: String = ""
    fun build() = AppMetadata(title, headline)
}

fun OutpostConfigBuilder.app(builder: AppMetadataBuilder.() -> Unit) {
    appMetadata = AppMetadataBuilder().apply(builder).build()
}
