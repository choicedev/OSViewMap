package com.choice.map.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.choice.core.extension.getCurrentLocation
import com.choice.design.component.MapScaffold
import com.choice.design.component.map.MapView
import com.choice.design.component.map.config.MapConfig.getUserIcon
import com.choice.design.component.rememberLifecycleObserver
import com.choice.location.service.TrackingService
import com.choice.location.util.ACTION_START_OR_RESUME_SERVICE
import com.choice.location.util.ACTION_STOP_SERVICE
import com.choice.map.MapViewModel
import com.choice.map.domain.LocationMarker
import com.choice.map.domain.MapEvent
import com.choice.map.domain.MapStateUi
import com.choice.map.domain.MarkerInfo
import com.choice.map.ui.composable.MapFloatingAction
import com.choice.map.util.MarkerID
import com.choice.map.util.extension.defaultAnimation
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


@Composable
fun MapUI(navHostController: NavHostController) {

    val viewModel: MapViewModel = hiltViewModel()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var mapView by remember {
        mutableStateOf<MapView?>(null)
    }

    var locationMarker by remember {
        mutableStateOf(LocationMarker())
    }

    val lifecycleObserver = rememberLifecycleObserver(
        onCreate = {
            context.getCurrentLocation(
                permissionFailed = {
                    viewModel.onEvent(MapEvent.ChangeState(MapStateUi.RequestPermission))
                },
                onSuccess = { lat, long ->
                    locationMarker = LocationMarker(
                        mapView = mapView,
                        user = MarkerInfo(
                            latitude = lat,
                            longitude = long,
                            id = MarkerID.USER_LOCATION,
                            marker = Marker(mapView),
                            icon = context.getUserIcon(),
                        ),
                    )


                    locationMarker.user?.updateMarker(mapView!!) { controller, info ->
                        controller.defaultAnimation(info.position)
                    }
                },
                onFailure = {
                    viewModel.onEvent(MapEvent.ChangeState(MapStateUi.RequestPermission))
                }
            )
        },
        onResume = {

        }
    )
    val tracking = TrackingService.LatLong.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = tracking.value){
        locationMarker.user?.copy(
            latitude = tracking.value.first,
            longitude = tracking.value.second
        )?.updateMarker(mapView!!) { controller, info ->
            controller.defaultAnimation(info.position)
        }
    }



    MapScaffold(
        navController = navHostController,
        navigation = viewModel.navigate,
        floatingActionButton = {
            MapFloatingAction(
                state = state.uiStateUi,
                mapView = mapView,
                onClick = { lat, long ->
                    viewModel.onEvent(
                        MapEvent.ChangeState(
                            MapStateUi.LocationMarker(
                                lat,
                                long
                            )
                        )
                    )
                    locationMarker.user?.copy(
                        latitude = lat,
                        longitude = long
                    )?.updateMarker(mapView!!) { controller, info ->
                        controller.animateTo(info.position)
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            MapView(Modifier.fillMaxSize()) { mView ->
                mapView = mView
                locationMarker.mapView = mView
            }


            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    if(!TrackingService.isTracking.value){
                        context.sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
                    }else{
                        context.sendCommandToService(ACTION_STOP_SERVICE)
                    }
                }) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = null
                )
            }


        }
    }


    DisposableEffect(lifecycleObserver) {
        onDispose { lifecycle.removeObserver(lifecycleObserver) }
    }
}

fun Context.sendCommandToService(action: String) {

    Intent(this, TrackingService::class.java).also {
        it.action = action
        this.startService(it)
    }

}




