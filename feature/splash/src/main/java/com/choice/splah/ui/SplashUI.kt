package com.choice.splah.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.choice.design.component.MapScaffold
import com.choice.design.theme.MapTheme
import com.choice.feature.navigation.Destination
import com.choice.splah.SplashViewModel
import com.choice.splash.BuildConfig
import com.choice.splash.R
import kotlinx.coroutines.delay

@Composable
fun SplashUI(navHostController: NavHostController) {
    val viewModel: SplashViewModel = hiltViewModel()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_animation))
    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit){
        delay(300)
        visible = !visible
        delay(3000)
        viewModel.navigateTo(Destination.MapScreen.fullRoute)
    }

    MapScaffold(
        navController = navHostController,
        navigation = viewModel.navigate
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LottieAnimation(
                composition = composition,
                iterations = 1
            )

            TextFadeAnimation(visible)
        }
    }


}

@Composable
fun TextFadeAnimation(
    visible: Boolean = true
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(2000)
        ) + fadeIn()
    ) {
        Text(
            text = BuildConfig.APP_NAME,
            style = MapTheme.typography.headlineMedium,
            color = MapTheme.colors.primary
        )
    }
}
