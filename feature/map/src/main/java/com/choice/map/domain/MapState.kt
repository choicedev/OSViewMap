package com.choice.map.domain

data class MapState(
    val uiStateUi: MapStateUi = MapStateUi.LOADING,
    val LatLong: Pair<Double, Double> = Pair(0.0, 0.0)
)