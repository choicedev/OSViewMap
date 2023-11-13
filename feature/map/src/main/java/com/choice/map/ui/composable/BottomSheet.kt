package com.choice.map.ui.composable

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.choice.design.theme.MapTheme
import com.choice.map.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BottomSheetCheckPermission(
    onGranted: () -> Unit,
    onDismiss: () -> Unit,
) {

    var showSheet by rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current


    val multiplePermission = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    ) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    if (multiplePermission.allPermissionsGranted) {
        showSheet = false
        onGranted()
    } else {
        BottomSheet(
            onDismiss = onDismiss
        ) {
            multiplePermission.launchMultiplePermissionRequest()
        }
    }

}

@Composable
private fun BottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberSheetState(),
    onAccept: () -> Unit,
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        dragHandle = {
            BottomSheetDefaults.DragHandle()
        }
    ) {

        Column(
            modifier = Modifier
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
                text = "Olá! Para melhorar a sua experiência e fornecer informações mais precisas, gostaríamos de solicitar a sua permissão para acessar a localização do seu celular. Isso nos ajudará a fornecer resultados mais relevantes com base na sua localização atual.",
                style = MapTheme.typography.bodyLarge,
                color = MapTheme.colors.onSurface,
                textAlign = TextAlign.Center
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MapTheme.spacing.mediumSmall),
                colors = ButtonDefaults.buttonColors(containerColor = MapTheme.colors.primary),
                onClick = onAccept
            ) {
                Text(
                    text = "Permitir Localização",
                    color = MapTheme.colors.surface
                )
            }
        }

    }

}