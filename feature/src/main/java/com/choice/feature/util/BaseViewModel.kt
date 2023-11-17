package com.choice.feature.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.LineBreak
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choice.feature.navigation.navigator.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel<STATE, EVENT>(
    private val initState: STATE,
    private val appNavigation: AppNavigator
) : ViewModel() {

    protected val _state = MutableStateFlow(initState)
    val uiState: StateFlow<STATE> get() = _state.stateIn(viewModelScope, SharingStarted.Lazily, initState)

    val navigate = appNavigation.navigationChannel

    fun navigateBack(
        route: String? = null,
        inclusive: Boolean = true
    ) {
        appNavigation.tryNavigateBack(
            route,
            inclusive
        )
    }

    fun navigateTo(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = true,
        isSingleTop: Boolean = false
    ) {
        appNavigation.tryNavigateTo(
            route,
            popUpToRoute,
            inclusive,
            isSingleTop
        )
    }

    abstract fun onEvent(event: EVENT)

}