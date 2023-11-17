package com.choice.map.ui

import android.Manifest
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationSearching
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.choice.core.extension.getCurrentLocation
import com.choice.core.extension.openSettings
import com.choice.design.component.MapScaffold
import com.choice.design.component.map.MapView
import com.choice.design.component.map.config.MapConfig
import com.choice.design.component.map.config.MapConfig.getUserIcon
import com.choice.design.component.rememberLifecycleObserver
import com.choice.design.theme.MapTheme
import com.choice.map.MapViewModel
import com.choice.map.R
import com.choice.map.domain.MapEvent
import com.choice.map.domain.MapStateUi
import com.choice.map.ui.composable.RequestPermissionUI
import com.choice.map.ui.composable.permissionList
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapUI(navHostController: NavHostController) {

    val viewModel: MapViewModel = hiltViewModel()
    val context = LocalContext.current
    val multiplePermission = rememberMultiplePermissionsState(permissions = permissionList)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val uiState = state.uiStateUi
    var mapView by remember {
        mutableStateOf<MapView?>(null)
    }

    var marker by remember {
        mutableStateOf<Marker?>(null)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle
    val lifeCycle = rememberLifecycleObserver(
        onResume = {
            if(multiplePermission.allPermissionsGranted){
                viewModel.onEvent(MapEvent.CHANGE_SCREEN(MapStateUi.LOADING))
                return@rememberLifecycleObserver
            }
            viewModel.onEvent(MapEvent.CHANGE_SCREEN(MapStateUi.REQUEST_PERMISSION))
        },
        onPause = {
            if(multiplePermission.allPermissionsGranted){
                viewModel.onEvent(MapEvent.CHANGE_SCREEN(MapStateUi.LOADING))
                return@rememberLifecycleObserver
            }
            viewModel.onEvent(MapEvent.CHANGE_SCREEN(MapStateUi.REQUEST_PERMISSION))

        }
    )

    MapScaffold(
        navController = navHostController,
        navigation = viewModel.navigate,
        floatingActionButton = {
            if (uiState is MapStateUi.MAPVIEW) {
                FloatingActionButton(onClick = {
                    context.getCurrentLocation(
                        permissionFailed = {
                            viewModel.onEvent(MapEvent.CHANGE_SCREEN(MapStateUi.REQUEST_PERMISSION))
                        },
                        onSuccess = { lat, long ->
                            updateMarkerAndMap(
                                lat,
                                long,
                                context,
                                mapView,
                                marker
                            )
                        },
                        onFailure = {
                            viewModel.onEvent(MapEvent.CHANGE_SCREEN(MapStateUi.REQUEST_PERMISSION))
                        }
                    )
                }) {
                    Icon(imageVector = Icons.Outlined.LocationSearching, contentDescription = null)
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            when (uiState) {
                is MapStateUi.REQUEST_PERMISSION -> {
                    RequestPermissionUI(modifier = Modifier.fillMaxSize())
                }

                is MapStateUi.MAPVIEW -> {
                    MapView(Modifier.fillMaxSize()) { view ->
                        mapView = view
                        marker = Marker(mapView)
                        view.controller.setCenter(uiState.currentLocationUser)
                        view.controller.animateTo(
                            uiState.currentLocationUser,
                            MapConfig.zoom.animation,
                            1500L
                        )
                        marker?.position = uiState.currentLocationUser
                        marker?.icon = context.getUserIcon()
                        view.overlays.add(marker)
                    }
                }

                is MapStateUi.LOADING -> {
                    context.getCurrentLocation(
                        permissionFailed = {
                            viewModel.onEvent(MapEvent.CHANGE_SCREEN(MapStateUi.REQUEST_PERMISSION))
                        },
                        onSuccess = { lat, long ->
                            viewModel.onEvent(MapEvent.CHANGE_SCREEN(MapStateUi.MAPVIEW(lat, long)))
                        },
                        onFailure = {
                            viewModel.onEvent(MapEvent.CHANGE_SCREEN(MapStateUi.LOADING))
                        }
                    )
                }

            }
        }
    }

    DisposableEffect(lifeCycle) {
        onDispose { lifecycle.removeObserver(lifeCycle) }
    }
}


private fun updateMarkerAndMap(
    lat: Double,
    long: Double,
    context: Context,
    mapView: MapView?,
    mark: Marker?
) {
    val point = GeoPoint(lat, long)
    mark?.position = point
    mark?.icon = context.getUserIcon()
    mapView?.controller?.setCenter(point)
    mapView?.controller?.animateTo(point, MapConfig.zoom.animation, 2500L)
    mapView?.overlays?.remove(mark)
    mapView?.overlays?.add(mark)
}

