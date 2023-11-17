package com.choice.design.component.map.config

import org.osmdroid.util.GeoPoint

data class MapCoords(
    val default: GeoPoint = GeoPoint(0.0, 0.0)
)