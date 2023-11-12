package com.choice.osviewmap.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import com.choice.feature.compose.MapNavHost
import com.choice.splah.navigation.splashComposable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun MapNavigationHost(systemColor: ColorScheme) {

    val navController = rememberAnimatedNavController()

    MapNavHost(
        navController = navController,
    ) {
        splashComposable(navController)
    }
}