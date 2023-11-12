package com.choice.splah

import com.choice.feature.navigation.navigator.AppNavigator
import com.choice.feature.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    appNavigator: AppNavigator
) : BaseViewModel<Unit, Unit>(Unit, appNavigator) {
    override fun onEvent(event: Unit) {

    }
}