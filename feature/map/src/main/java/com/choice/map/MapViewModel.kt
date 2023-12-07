package com.choice.map

import com.choice.feature.navigation.navigator.AppNavigator
import com.choice.feature.util.BaseViewModel
import com.choice.location.service.TrackingService
import com.choice.map.domain.MapEvent
import com.choice.map.domain.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    appNavigation: AppNavigator
): BaseViewModel<MapState, MapEvent>(MapState(), appNavigation) {


    private var job: Job = SupervisorJob()
    init {

        TrackingService.LatLong.onEach { loc ->
            _state.update {
                it.copy(
                    LatLong = Pair(loc.first, loc.second)
                )
            }
        }.stateIn(CoroutineScope(Dispatchers.IO + job), SharingStarted.Eagerly, 1)

    }

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