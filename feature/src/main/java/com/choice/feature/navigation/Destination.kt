package com.choice.feature.navigation

sealed class Destination(
    protected val route: String,
    vararg params: String
) {

    object SplashScreen : NoArgumentsDestination("splash")
    object MapScreen : NoArgumentsDestination("map")

    val fullRoute: String = if (params.isEmpty()) route
    else StringBuilder(route).let { builder ->
        params.forEach { p -> builder.append("/{${p}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke() = route
    }

    internal fun String.appendParams(vararg params: Pair<String, Any?>): String =
        StringBuilder(this).let { builder ->
            params.forEach { p ->
                p.second?.toString()?.let { args ->
                    builder.append("/$args")
                }
            }
            builder.toString()
        }
}