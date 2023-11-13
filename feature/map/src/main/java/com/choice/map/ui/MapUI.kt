package com.choice.map.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.choice.design.component.MapScaffold
import com.choice.map.MapViewModel
import com.choice.map.ui.composable.BottomSheetCheckPermission
import com.choice.map.ui.composable.MapView
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapUI(navHostController: NavHostController) {

    val viewModel: MapViewModel = hiltViewModel()

   var showSheet by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    var mapView: MapView? by remember {
        mutableStateOf(null)
    }

    if(showSheet){
        BottomSheetCheckPermission(
            onDismiss = {
                showSheet = false
            },
            onGranted = {
                showSheet = false
                getCurrentLocation(context){ lat, long ->
                    val point = GeoPoint(lat, long)
                    val mark = Marker(mapView)
                    mapView?.overlays?.clear()
                    mark.position = point
                    mapView?.controller?.setCenter(point)
                    mapView?.controller?.animateTo(point, 20.0, 15L)
                    mapView?.overlays?.add(mark)
                }

             }
        )
    }
    MapScaffold(
        navController = navHostController,
        navigation = viewModel.navigate
    ){

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){

            MapView(Modifier.fillMaxSize()) { mapViews ->

                mapView = mapViews
                val point = GeoPoint(-23.72449, -46.53960)
                val mark = Marker(mapView)
                mark.position = point
                mapViews.controller.setCenter(point)
                mapViews.controller.animateTo(point, 15.0, 5L)
                mapViews.overlays.add(mark)


            }

            Button(modifier = Modifier.align(Alignment.TopCenter),onClick = {
                showSheet = true
            }) {
                Text(
                    text = "Localização Atual"
                )
            }
        }

    }

}


private fun getCurrentLocation(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                callback(lat, long)
            }
        }
        .addOnFailureListener { exception ->
            exception.printStackTrace()
        }
}