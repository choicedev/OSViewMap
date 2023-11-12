package com.choice.map.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.choice.feature.compose.applicationComposable
import com.choice.feature.navigation.Destination
import com.choice.map.ui.MapUI

fun NavGraphBuilder.mapComposable(
    navController: NavHostController,
) {
    applicationComposable(
        destination = Destination.MapScreen,
    ) {
        MapUI(navController)
    }
}