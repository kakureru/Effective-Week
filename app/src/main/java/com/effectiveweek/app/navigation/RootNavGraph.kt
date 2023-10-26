package com.effectiveweek.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.effectiveweek.schedule.presentation.navigation.scheduleScreen

@Composable
fun RootNavGraph(navHostController: NavHostController, startDestination: String) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        scheduleScreen(navController = navHostController)
    }
}