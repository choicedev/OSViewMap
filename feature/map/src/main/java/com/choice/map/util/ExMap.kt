package com.choice.map.util

import android.content.Context
import com.choice.design.component.map.config.MapConfig
import com.choice.design.component.map.config.MapConfig.getUserIcon
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


fun MapView.updateMarkerAndMap(
    lat: Double,
    long: Double,
    context: Context,
    mark: Marker?
) {
    val point = GeoPoint(lat, long)
    mark?.position = point
    mark?.icon = context.getUserIcon()
    controller?.setCenter(point)
    controller?.animateTo(point, MapConfig.zoom.animation, 2500L)
    overlays?.remove(mark)
    overlays?.add(mark)
}