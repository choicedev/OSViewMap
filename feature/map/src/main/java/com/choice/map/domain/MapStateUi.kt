package com.choice.map.domain

import org.osmdroid.util.GeoPoint

sealed class MapStateUi {

    object LOADING : MapStateUi()
    object REQUEST_PERMISSION : MapStateUi()
    data class MAPVIEW(val lat: Double, val long: Double) : MapStateUi(){
        val currentLocationUser = GeoPoint(
            lat,
            long
        )
    }

}