package com.choice.core.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

fun Context.openSettings() {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    } else {
        TODO("VERSION.SDK_INT < GINGERBREAD")
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri: Uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}



