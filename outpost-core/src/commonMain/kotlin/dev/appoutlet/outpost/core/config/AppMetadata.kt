package dev.appoutlet.outpost.core.config

data class AppMetadata(
    val title: String,
    val headline: String,
)

class AppMetadataBuilder {
    var title: String = ""
        get(): String {
            require(field.isNotBlank()) { "Title cannot be blank" }
            return field
        }

    var headline: String = ""
        get() {
            require(field.isNotBlank()) { "Headline cannot be blank" }
            return field
        }

    fun build() = AppMetadata(title, headline)
}

fun OutpostConfigBuilder.app(builder: AppMetadataBuilder.() -> Unit) {
    appMetadata = AppMetadataBuilder().apply(builder).build()
}