package com.effectiveweek.app.navigation

sealed class RootRoute(val route: String) {
    object Schedule : RootRoute(route = "schedule")
}