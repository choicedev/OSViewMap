package com.choice.map.domain

import android.graphics.drawable.Drawable
import com.choice.map.util.MarkerID
import org.osmdroid.api.IMapController
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


data class MarkerInfo (
    val id: String,
    val marker: Marker,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val title: String = "",
    val snippet: String = "",
    val icon: Drawable? = null
){
    val position = GeoPoint(latitude, longitude)

    fun updateMarker(
        mapView: MapView,
        mapController: ((IMapController, MarkerInfo) -> Unit?)? = null) {
        mapView.overlays.remove(marker)
        marker.position = position
        marker.icon = icon
        marker.id = id
        marker.title = title
        if(id == MarkerID.USER_LOCATION){
            mapView.overlays.add(marker)
        }else{
            mapView.overlays.add(0, marker)
        }
        mapController?.invoke(mapView.controller, this)
    }
}