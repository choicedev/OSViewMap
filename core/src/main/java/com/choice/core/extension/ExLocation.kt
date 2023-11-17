package com.choice.core.extension

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

/*
    Get current location from user
*/
fun Context.getCurrentLocation(
    permissionFailed: () -> Unit = {},
    onSuccess: (lat: Double, long: Double) -> Unit,
    onFailure: (Throwable) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        permissionFailed()
        return
    }
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                onSuccess(lat, long)
            }
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}