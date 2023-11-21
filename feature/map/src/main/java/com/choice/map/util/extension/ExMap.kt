package com.choice.map.util.extension

import android.content.Context
import com.choice.design.component.map.config.MapConfig
import com.choice.design.component.map.config.MapConfig.getUserIcon
import org.osmdroid.api.IMapController
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


fun IMapController.defaultAnimation(
    geoPoint: GeoPoint,
    center: GeoPoint? = null
){

    this.animateTo(geoPoint, MapConfig.zoom.animation, 2500L)
    center?.let {
        this.setCenter(it)
    }
}