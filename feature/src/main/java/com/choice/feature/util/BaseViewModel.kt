package com.choice.feature.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.choice.feature.navigation.navigator.AppNavigator

abstract class BaseViewModel<STATE, EVENT>(
    initState: STATE,
    private val appNavigation: AppNavigator
) : ViewModel() {

    var state by mutableStateOf(initState)

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