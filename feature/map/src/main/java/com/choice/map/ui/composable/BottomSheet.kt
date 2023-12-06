package com.choice.map.ui.composable

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.choice.design.theme.MapTheme
import com.choice.map.domain.LocationMarker
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun MyLocationBottomSheet(
    sheetState: SheetState,
    locationMarker: LocationMarker,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit
) {

    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest) {
        Text(
            text = "Definir como:"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MapTheme.spacing.mediumSmall),
        ) {

            Button(onClick = {

            }) {

                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null
                )

                Text(
                    text = "Casa"
                )


            }

            Button(onClick = { }) {

                Icon(
                    imageVector = Icons.Filled.MoreHoriz,
                    contentDescription = null
                )

                Text(
                    text = "Outro"
                )

            }

        }

    }
}