package com.choice.map.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.scaleMatrix
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.choice.core.extension.getCurrentLocation
import com.choice.design.component.MapScaffold
import com.choice.design.component.rememberLifecycleObserver
import com.choice.map.MapViewModel
import com.choice.map.R
import com.choice.map.ui.composable.BottomSheetCheckPermission
import com.choice.map.ui.composable.MapView
import com.choice.map.ui.composable.getLottieIconForUser
import com.choice.map.util.MapConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapUI(navHostController: NavHostController) {

    val viewModel: MapViewModel = hiltViewModel()

    var mapView: MapView? by remember {
        mutableStateOf(null)
    }

    var mark: Marker? by remember {
        mutableStateOf(null)
    }

    val context = LocalContext.current


    CheckPermission(onSuccess = { lat, long ->
        val point = GeoPoint(lat, long)
        mapView?.overlays?.clear()
        mark?.position = point
        mark!!.icon = ResourcesCompat.getDrawable(context.resources, R.drawable.base_circle_border, null)
        mapView?.controller?.setCenter(point)
        mapView?.controller?.animateTo(point, 18.0, 1500L)
        mapView?.overlays?.add(mark)
    })

    MapScaffold(
        navController = navHostController,
        navigation = viewModel.navigate
    ){

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){

            MapView(Modifier.fillMaxSize()) { mapViews ->
                mapView = mapViews
                mark = Marker(mapView)
                val point = GeoPoint(0, 0)
                mapViews.controller.setCenter(MapConfig.defaultCoords)
                mapViews.controller.animateTo(point, 4.0, 5L)
            }
        }

    }

}

@Composable
private fun CheckPermission(
    onSuccess: (lat: Double, long: Double) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle

    var showSheet by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        showSheet = true
    }


    if (showSheet) {
        BottomSheetCheckPermission(
            onGranted = {
                showSheet = false
                context.getCurrentLocation(
                    permissionFailed = {
                        Toast.makeText(context, "Not accept", Toast.LENGTH_LONG).show()
                    },
                    isSuccess = { lat, long ->
                        onSuccess(lat, long)
                    },
                    isFailure = { throwable ->
                        Toast.makeText(context, throwable.message ?: "", Toast.LENGTH_LONG).show()
                    }
                )
            },
            onDismiss = {
                showSheet = false
            }
        )
    }
}
