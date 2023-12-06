package com.choice.map.ui

import android.content.Context
import android.content.Intent
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
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
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
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
                    viewModel.onEvent(
                        MapEvent.ChangeState(
                            MapStateUi.LocationMarker(
                                lat,
                                long
                            )
                        )
                    )

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


    var trackingUser by remember {
        mutableStateOf(false)
    }

    val snackBar = SnackbarHostState()

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

                    if(locationMarker.user == null){
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
                    }

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

            FilledIconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    trackingUser = !trackingUser
                }) {
                val icon =
                    if (trackingUser) Icons.Filled.MyLocation else Icons.Filled.LocationSearching
                Icon(imageVector = icon, contentDescription = null)
            }

        }
    }

    ForegroundLocationTracker {

        mapView?.let { mapView ->
            locationMarker.user?.copy(
                latitude = it.latitude,
                longitude = it.longitude
            )?.updateMarker(mapView) { controller, info ->
                if (trackingUser) controller.defaultAnimation(info.position) else mapView.postInvalidate()
            }
        }

    }


    DisposableEffect(lifecycleObserver) {
        onDispose { lifecycle.removeObserver(lifecycleObserver) }
    }
}






