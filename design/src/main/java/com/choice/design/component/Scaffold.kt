package com.choice.design.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldDefaults
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.choice.design.theme.MapTheme
import com.choice.feature.navigation.NavigationEffects
import com.choice.feature.navigation.NavigationIntent
import kotlinx.coroutines.channels.Channel
import java.io.IOException


@Composable
fun MapScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    navigation: Channel<NavigationIntent>? = null,
    navController: NavHostController? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MapTheme.colors.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit
) {

    navigation?.let {
        navController?.let { nav ->
            NavigationEffects(
                navigationChannel = it,
                navHostController = nav
            )
        } ?: throw IOException("navigation without navController")
    }


    Scaffold(
        modifier,
        topBar,
        bottomBar,
        snackbarHost = {},
        floatingActionButton,
        floatingActionButtonPosition,
        containerColor,
        contentColor,
        contentWindowInsets,
        content
    )
}


@Composable
fun MapBottomSheetScaffold(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    navigation: Channel<NavigationIntent>? = null,
    navController: NavHostController? = null,
    topBar: (@Composable () -> Unit)? = null,
    snackbarHost: @Composable (SnackbarHostState) -> Unit = {
        androidx.compose.material.SnackbarHost(
            it
        )
    },
    floatingActionButton: (@Composable () -> Unit)? = null,
    floatingActionButtonPosition: androidx.compose.material.FabPosition = androidx.compose.material.FabPosition.End,
    sheetGesturesEnabled: Boolean = true,
    sheetShape: Shape = MapTheme.shapes.large,
    sheetElevation: Dp = 0.dp,
    sheetBackgroundColor: Color = MapTheme.colors.surface,
    sheetContentColor: Color = androidx.compose.material.contentColorFor(sheetBackgroundColor),
    sheetPeekHeight: Dp = 40.dp,
    drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    drawerGesturesEnabled: Boolean = true,
    drawerShape: Shape = MapTheme.shapes.large,
    drawerElevation: Dp = DrawerDefaults.Elevation,
    drawerBackgroundColor: Color = MapTheme.colors.surface,
    drawerContentColor: Color = androidx.compose.material.contentColorFor(drawerBackgroundColor),
    drawerScrimColor: Color = DrawerDefaults.scrimColor,
    backgroundColor: Color = MapTheme.colors.background,
    contentColor: Color = androidx.compose.material.contentColorFor(backgroundColor),
    content: @Composable (PaddingValues) -> Unit
) {

    navigation?.let {
        navController?.let { nav ->
            NavigationEffects(
                navigationChannel = it,
                navHostController = nav
            )
        } ?: throw IOException("navigation without navController")
    }


    BottomSheetScaffold(
        sheetContent,
        modifier,
        scaffoldState,
        topBar,
        snackbarHost,
        floatingActionButton,
        floatingActionButtonPosition,
        sheetGesturesEnabled,
        sheetShape,
        sheetElevation,
        sheetBackgroundColor,
        sheetContentColor,
        sheetPeekHeight,
        drawerContent,
        drawerGesturesEnabled,
        drawerShape,
        drawerElevation,
        drawerBackgroundColor,
        drawerContentColor,
        drawerScrimColor,
        backgroundColor,
        contentColor,
        content
    )
}
