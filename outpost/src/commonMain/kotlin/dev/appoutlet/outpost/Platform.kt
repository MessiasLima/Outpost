package dev.appoutlet.outpost

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform