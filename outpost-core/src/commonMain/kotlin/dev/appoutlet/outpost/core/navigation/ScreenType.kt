package dev.appoutlet.outpost.core.navigation

sealed interface ScreenType {
    interface Media : ScreenType
    interface MediaList : ScreenType
}
