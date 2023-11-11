package com.effectiveweek.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.effectiveweek.schedule.presentation.navigation.ScheduleNavHost

@Composable
fun RootNavGraph(navHostController: NavHostController, startDestination: String) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(route = RootRoute.Schedule.route) {
            ScheduleNavHost()
        }
    }
}