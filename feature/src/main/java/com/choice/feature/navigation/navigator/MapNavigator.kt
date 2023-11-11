package com.choice.feature.navigation.navigator

import com.choice.feature.navigation.NavigationIntent
import kotlinx.coroutines.channels.Channel

interface AppNavigator {

    val navigationChannel: Channel<NavigationIntent>

    suspend fun navigateBack(
        route: String? = null,
        inclusive: Boolean = true
    )

    fun tryNavigateBack(
        route: String? = null,
        inclusive: Boolean = true
    )

    suspend fun navigateTo(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = true,
        isSingleTop: Boolean = true
    )

    fun tryNavigateTo(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = true,
        isSingleTop: Boolean = true,
    )

}