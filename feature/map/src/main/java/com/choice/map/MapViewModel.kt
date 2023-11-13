package com.choice.map

import com.choice.feature.navigation.navigator.AppNavigator
import com.choice.feature.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    appNavigation: AppNavigator
): BaseViewModel<Unit, Unit>(Unit, appNavigation) {
    override fun onEvent(event: Unit) {

    }
}