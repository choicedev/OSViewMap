package com.choice.map.ui.composable

import android.Manifest
import android.Manifest.*
import android.Manifest.permission.*
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.choice.core.extension.getCurrentLocation
import com.choice.core.extension.openSettings
import com.choice.design.theme.MapTheme
import com.choice.map.MapViewModel
import com.choice.map.R
import com.choice.map.domain.MapEvent
import com.choice.map.domain.MapStateUi
import com.choice.map.util.updateMarkerAndMap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

val permissionList = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionUI(
    modifier: Modifier = Modifier,
) {
    val viewModel: MapViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val multiplePermission = rememberMultiplePermissionsState(permissions = permissionList)

    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(MapTheme.spacing.mediumSmall),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.current_location),
            contentDescription = "",
            modifier = Modifier
                .size(MapTheme.spacing.giantMedium)
                .padding(bottom = 12.dp)
        )

        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.get_permission_location),
            style = MapTheme.typography.bodyLarge,
            color = MapTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MapTheme.spacing.mediumSmall),
            colors = ButtonDefaults.buttonColors(containerColor = MapTheme.colors.primary),
            onClick = {

                if(multiplePermission.allPermissionsGranted){
                    viewModel.onEvent(MapEvent.CHANGE_SCREEN(MapStateUi.LOADING))
                }

                if(!multiplePermission.allPermissionsGranted && !multiplePermission.shouldShowRationale){
                    context.openSettings()
                    return@Button
                }

                multiplePermission.launchMultiplePermissionRequest()
            }
        ) {
            Text(
                text = stringResource(id = R.string.enable_localization),
                color = MapTheme.colors.surface
            )
        }
    }


}