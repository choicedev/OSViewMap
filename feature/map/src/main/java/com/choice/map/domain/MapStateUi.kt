package com.choice.map.domain

import org.osmdroid.util.GeoPoint

sealed class MapStateUi {

    object LOADING : MapStateUi()
    object RequestPermission : MapStateUi()
    data class LocationMarker(val lat: Double = 0.0, val long: Double = 0.0) : MapStateUi(){
        val currentLocationUser = GeoPoint(
            lat,
            long
        )
    }

}