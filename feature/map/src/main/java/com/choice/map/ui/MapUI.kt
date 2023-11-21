package com.choice.map.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.rememberSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.choice.core.extension.getCurrentLocation
import com.choice.design.component.MapScaffold
import com.choice.design.component.map.MapView
import com.choice.design.component.map.config.MapConfig
import com.choice.design.component.map.config.MapConfig.getUserIcon
import com.choice.design.component.rememberLifecycleObserver
import com.choice.map.MapViewModel
import com.choice.map.domain.LocationMarker
import com.choice.map.domain.MapEvent
import com.choice.map.domain.MapStateUi
import com.choice.map.domain.MarkerInfo
import com.choice.map.ui.composable.MapFloatingAction
import com.choice.map.ui.composable.MyLocationBottomSheet
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
    val sheetState = rememberSheetState()
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var showMyLocationBottomSheet by remember {
        mutableStateOf(false)
    }

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
                    viewModel.onEvent(MapEvent.ChangeState(MapStateUi.LocationMarker(lat, long)))

                    locationMarker = LocationMarker(
                        mapView = mapView,
                        user = MarkerInfo(
                            latitude = lat,
                            longitude = long,
                            id = MarkerID.USER_LOCATION,
                            marker = Marker(mapView).apply {
                                setOnMarkerClickListener { marker, mapView ->
                                    if (marker.id == MarkerID.USER_LOCATION) {
                                        if (mapView.zoomLevelDouble in (MapConfig.zoom.min .. 5.9)) {
                                            mapView.controller.defaultAnimation(marker.position)
                                        }

                                        locationMarker = locationMarker.copy(
                                            user = locationMarker.user?.copy(
                                                latitude = marker.position.latitude,
                                                longitude = marker.position.longitude
                                            )
                                        )

                                        showMyLocationBottomSheet = true
                                    }

                                    return@setOnMarkerClickListener true
                                }
                            },
                            icon = context.getUserIcon(),
                        ),
                        home = MarkerInfo(
                            id = "user_home",
                            marker = Marker(mapView),
                            latitude = -43.900,
                            longitude = -39.555,
                            title = "Your home"
                        )
                    )


                    locationMarker.user?.updateMarker(mapView!!) { controller, info ->
                        controller.defaultAnimation(info.position)
                    }
                    locationMarker.home?.updateMarker(mapView!!)
                },
                onFailure = {
                    viewModel.onEvent(MapEvent.ChangeState(MapStateUi.RequestPermission))
                }
            )
        }
    )

    DisposableEffect(lifecycleObserver) {
        onDispose { lifecycle.removeObserver(lifecycleObserver) }
    }

    if (showMyLocationBottomSheet) {
        MyLocationBottomSheet(
            locationMarker = locationMarker,
            sheetState = sheetState
        ) {
            showMyLocationBottomSheet = false
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
                    (0..10).random().let {
                        viewModel.onEvent(
                            MapEvent.ChangeState(
                                MapStateUi.LocationMarker(
                                    lat + it,
                                    long + it
                                )
                            )
                        )
                        locationMarker.user?.copy(
                            latitude = lat + it,
                            longitude = long + it
                        )?.updateMarker(mapView!!) { controller, info ->
                            controller.animateTo(info.position)
                        }
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


        }
    }
}




