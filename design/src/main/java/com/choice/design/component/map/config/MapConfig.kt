package com.choice.design.component.map.config

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.choice.design.R

object MapConfig {

    val zoom: MapZoom
        get() = MapZoom()

    val coords: MapCoords
        get() = MapCoords()


    fun Context.getUserIcon() = ResourcesCompat.getDrawable(resources, R.drawable.base_circle_border, null)

}