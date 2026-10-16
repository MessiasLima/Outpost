package dev.appoutlet.outpost.core.config

@OutpostConfigDsl
class OutpostConfigContentBuilder {
    private val types = mutableListOf<Content>()

    fun add(type: Content) {
        types += type
    }

    fun build(): List<Content> {
        return types.toList()
    }
}
