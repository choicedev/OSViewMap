package com.choice.splah.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.choice.feature.compose.applicationComposable
import com.choice.feature.navigation.Destination

fun NavGraphBuilder.splashComposable(
    navController: NavHostController,
) {
    applicationComposable(
        destination = Destination.SplashScreen
    ) {
        TODO()
    }
}