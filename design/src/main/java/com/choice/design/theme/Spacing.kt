package com.choice.design.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val default: Dp = 2.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val mediumSmall: Dp = 16.dp,
    val largeSmall: Dp = 24.dp,
    val extraMedium: Dp = 32.dp,
    val medium: Dp = 38.dp,
    val largeMedium: Dp = 42.dp,
    val extraLarge: Dp = 44.dp,
    val large: Dp = 48.dp,
    val largeLarge: Dp = 84.dp,
    val giantSmall: Dp = 100.dp,
    val giantMedium: Dp = 150.dp,
    val giantLarge: Dp = 200.dp,
)

internal val LocalSpacing = compositionLocalOf { Spacing() }