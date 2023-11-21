package com.choice.map.domain

sealed class MapEvent {
    data class ChangeState(val screen: MapStateUi) : MapEvent()
}