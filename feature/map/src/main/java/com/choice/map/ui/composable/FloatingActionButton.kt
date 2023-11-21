package com.choice.map.ui.composable

import android.Manifest
import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.NearMeDisabled
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.choice.core.extension.getCurrentLocation
import com.choice.core.extension.openSettings
import com.choice.design.theme.MapTheme
import com.choice.map.domain.MapStateUi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.osmdroid.views.MapView


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapFloatingAction(
    state: MapStateUi,
    mapView: MapView?,
    modifier: Modifier = Modifier,
    onClick: (Double, Double) -> Unit,
) {
    val context = LocalContext.current
    val multiplePermission =
        rememberMultiplePermissionsState(permissions = permissionList) { perms ->

            if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == true && perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                onClickState(
                    null,
                    context = context,
                    onSuccess = onClick,
                    onFailed = {
                        context.openSettings()
                    }
                )
            } else {
                context.openSettings()
            }
        }

    val iconState =
        if (state is MapStateUi.LocationMarker) {
            Icons.Outlined.NearMe
        } else {
            Icons.Outlined.NearMeDisabled
        }

    val iconColor =
        if (state is MapStateUi.LocationMarker) {
            MapTheme.colors.onPrimaryContainer
        } else {
            MapTheme.colors.error
        }


    Column(
        modifier = modifier
            .animateContentSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.End
    ) {

/*
        ZoomColumn(
            mapView = mapView
        )

        Spacer(modifier = Modifier.padding(vertical = MapTheme.spacing.mediumSmall))
*/

        FloatingActionButton(
            modifier = Modifier
                .padding(horizontal = MapTheme.spacing.extraSmall)
                .padding(bottom = MapTheme.spacing.mediumSmall),
            containerColor = MapTheme.colors.primaryContainer,
            onClick = {
                onClickState(
                    multiplePermission,
                    context = context,
                    onSuccess = onClick,
                    onFailed = {
                        context.openSettings()
                    })
            }) {

            Icon(
                imageVector = iconState,
                tint = iconColor,
                contentDescription = null
            )
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
fun onClickState(
    multiplePermission: MultiplePermissionsState? = null,
    context: Context,
    onSuccess: (Double, Double) -> Unit,
    onFailed: (Throwable) -> Unit,
) {

    context.getCurrentLocation(
        permissionFailed = {
            multiplePermission?.launchMultiplePermissionRequest()
        },
        onSuccess = onSuccess,
        onFailure = onFailed
    )

}

@Composable
private fun ZoomColumn(
    mapView: MapView?,
    modifier: Modifier = Modifier
) {

    var showZoomMax by remember {
        mutableStateOf(true)
    }

    var showZoomMin by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = modifier
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        FloatingActionButton(
            containerColor = if (showZoomMax) MapTheme.colors.secondaryContainer else Color.LightGray,
            modifier = Modifier
                .padding(MapTheme.spacing.small)
                .size(MapTheme.spacing.largeSmall + 5.dp),
            onClick = {
                showZoomMax = mapView?.canZoomIn() == true
                showZoomMin = mapView?.canZoomOut() == true
                mapView?.controller?.zoomIn()
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                tint = MapTheme.colors.onPrimaryContainer,
                contentDescription = null
            )
        }
    }

    Spacer(Modifier.padding(MapTheme.spacing.small))

    FloatingActionButton(
        containerColor = if (showZoomMin) MapTheme.colors.secondaryContainer else Color.LightGray,
        modifier = Modifier
            .padding(MapTheme.spacing.small)
            .size(MapTheme.spacing.largeSmall + 5.dp),
        onClick = {
            showZoomMax = mapView?.canZoomIn() == true
            showZoomMin = mapView?.canZoomOut() == true
            mapView?.controller?.zoomOut()
        }
    ) {
        Icon(
            imageVector = Icons.Outlined.Remove,
            tint = MapTheme.colors.onPrimaryContainer,
            contentDescription = null
        )
    }
}