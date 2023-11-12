package com.choice.map.ui

import android.preference.PreferenceManager
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.choice.design.component.MapScaffold
import com.choice.map.MapViewModel
import com.choice.map.ui.composable.MapView
import kotlinx.coroutines.delay
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

@Composable
fun MapUI(navHostController: NavHostController) {

    val viewModel: MapViewModel = hiltViewModel()

    val targetNumber = remember { mutableStateOf(6) }
    val animatedNumber by animateIntAsState(targetNumber.value, label = "")

    LaunchedEffect(key1 = true) {
        while (targetNumber.value < 15) {
            delay(50L) // delay for 1 second
            targetNumber.value++
        }
    }
    MapScaffold(
        navController = navHostController,
        navigation = viewModel.navigate
    ){

        Box(modifier = Modifier.fillMaxSize().padding(it)){

            MapView(Modifier.fillMaxSize()) { mapView ->

                val point = GeoPoint(-23.72449, -46.53960)
                val mark = Marker(mapView)
                mark.position = point
                mapView.controller.setCenter(point)
                mapView.controller.animateTo(point, 15.0, 5L)
                mapView.overlays.add(mark)


            }

            Button(modifier = Modifier.align(Alignment.TopCenter),onClick = {  }) {
                Text(
                    text = "BOTAO TESTE"
                )
            }
        }

    }

}
