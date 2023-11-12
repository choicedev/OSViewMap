package com.choice.map.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
        enterTransition = {
            fadeIn(tween(1000))
        }
    ) {
        MapUI(navController)
    }
}