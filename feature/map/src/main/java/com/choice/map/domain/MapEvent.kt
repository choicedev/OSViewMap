package com.choice.map.domain

sealed class MapEvent {
    data class CHANGE_SCREEN(val screen: MapStateUi) : MapEvent()
}