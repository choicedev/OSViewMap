package com.choice.location.compose

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


private const val TAG = "ForegroundLocationTracker"

private object SimulatedDisposableEffectResult : DisposableEffectResult {
    override fun dispose() {

    }
}

@OptIn(ExperimentalPermissionsApi::class)
private object SimulatedMultiplePermissionsState : MultiplePermissionsState {
    override val allPermissionsGranted: Boolean
        get() = false
    override val permissions: List<PermissionState>
        get() = emptyList()
    override val revokedPermissions: List<PermissionState>
        get() = emptyList()
    override val shouldShowRationale: Boolean
        get() = false

    override fun launchMultiplePermissionRequest() {

    }
}

@OptIn(ExperimentalPermissionsApi::class)
sealed interface LocationPermissionsState {
    @Composable
    operator fun invoke(): MultiplePermissionsState

    object CoarseAndFine : LocationPermissionsState {
        @Composable
        override fun invoke(): MultiplePermissionsState {
            return rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )
        }
    }
    
    object Simulated : LocationPermissionsState {
        @Composable
        override fun invoke(): MultiplePermissionsState {
            return remember {
                SimulatedMultiplePermissionsState
            }
        }
    }
}

private suspend fun requestToEnableGPS(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    val canNavigateToGPSSettings =
        intent.resolveActivity(context.packageManager) != null

    if (canNavigateToGPSSettings) {
        context.startActivity(intent)
    }
}

fun isGPSEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(
        Context.LOCATION_SERVICE
    ) as LocationManager

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun ForegroundLocationTracker(
    onLocationDenied: () -> Unit,
    onLocationUpdates: (Location) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var isGPSEnabled by remember {
        mutableStateOf(isGPSEnabled(context))
    }

    LaunchedEffect(true) {
        while (true) {
            isGPSEnabled = isGPSEnabled(context)
            delay(2.seconds)
        }
    }

    DisposableEffect(
        isGPSEnabled,
    ) {

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L,
        ).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(p0: LocationAvailability) {
                if (p0.isLocationAvailable) {
                    return super.onLocationAvailability(p0)
                }

                scope.launch {
                    requestToEnableGPS(
                        context = context
                    )
                }
            }

            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                p0.lastLocation?.also {
                    onLocationUpdates(it)
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper(),
        )

        onDispose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }
}