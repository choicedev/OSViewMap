package com.choice.splah.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.choice.feature.compose.applicationComposable
import com.choice.feature.navigation.Destination
import com.choice.splah.ui.SplashUI

fun NavGraphBuilder.splashComposable(
    navController: NavHostController,
) {
    applicationComposable(
        destination = Destination.SplashScreen,
        enterTransition = { fadeIn(tween(1000))},
        exitTransition = { fadeOut(tween(1000)) }
    ) {
        SplashUI(navController)
    }
}