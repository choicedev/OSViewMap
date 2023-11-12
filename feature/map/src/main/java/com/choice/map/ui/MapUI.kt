package com.choice.map.ui

import android.preference.PreferenceManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.choice.design.component.MapScaffold
import com.choice.map.MapViewModel
import com.choice.map.ui.composable.MapView
import org.osmdroid.config.Configuration

@Composable
fun MapUI(navHostController: NavHostController) {

    val viewModel: MapViewModel = hiltViewModel()

    MapScaffold(
        navController = navHostController,
        navigation = viewModel.navigate
    ){

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            MapView(Modifier.fillMaxSize())
        }

    }

}
