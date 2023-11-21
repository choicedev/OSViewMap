package com.choice.map

import com.choice.feature.navigation.navigator.AppNavigator
import com.choice.feature.util.BaseViewModel
import com.choice.map.domain.MapEvent
import com.choice.map.domain.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    appNavigation: AppNavigator
): BaseViewModel<MapState, MapEvent>(MapState(), appNavigation) {

    override fun onEvent(event: MapEvent) {
        when(event){
            is MapEvent.ChangeState -> {
                _state.update {
                    it.copy(
                        uiStateUi = event.screen
                    )
                }
            }
        }
    }
}