package com.choice.map

import androidx.compose.ui.text.style.LineBreak
import androidx.lifecycle.viewModelScope
import com.choice.feature.navigation.navigator.AppNavigator
import com.choice.feature.util.BaseViewModel
import com.choice.map.domain.MapEvent
import com.choice.map.domain.MapState
import com.choice.map.domain.MapStateUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    appNavigation: AppNavigator
): BaseViewModel<MapState, MapEvent>(MapState(), appNavigation) {

    override fun onEvent(event: MapEvent) {
        when(event){
            is MapEvent.CHANGE_SCREEN -> {
                _state.update {
                    it.copy(
                        uiStateUi = event.screen
                    )
                }
            }
        }
    }
}