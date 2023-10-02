package com.example.greatweek.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.example.schedule.presentation.goal_dialog.ui.GoalDialog
import com.example.schedule.presentation.role_dialog.ui.RoleDialog
import com.example.schedule.presentation.role_pick_dialog.ui.RolePickDialog
import com.example.schedule.presentation.schedule.ui.ScheduleScreen
import com.example.schedule.presentation.schedule.ui.previewGoalCallback
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RootNavGraph(navHostController: NavHostController, startDestination: String) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(route = Screen.Schedule.route) {
            ScheduleScreen(
                goalCallback = previewGoalCallback,
                onAddGoalToScheduleClick = {
                    with(Screen.GoalDialog) {
                        navHostController.navigate("$route?$ARG_DATE=$it")
                    }
                },
                onAddGoalToRoleClick = {
                    with(Screen.GoalDialog) {
                        navHostController.navigate("$route?$ARG_ROLE=$it")
                    }
                },
                onEditRoleClick = {
                    with(Screen.RoleDialog) {
                        navHostController.navigate("$route?$ARG_NAME=$it")
                    }
                },
                onAddRoleClick = {
                    navHostController.navigate(Screen.RoleDialog.route)
                },
                onDeleteRoleClick = {},
                viewModel = koinViewModel(),
            )
        }
        with(Screen.GoalDialog) {
            dialog(
                route = "$route?$ARG_ID={$ARG_ID}&$ARG_DATE={$ARG_DATE}&$ARG_ROLE={$ARG_ROLE}",
                arguments = listOf(
                    navArgument(ARG_ID) {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument(ARG_DATE) {
                        type = NavType.LongType
                        defaultValue = -1
                    },
                    navArgument(ARG_ROLE) {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) { entry ->
                GoalDialog(
                    viewModel = koinViewModel {
                        parametersOf(
                            entry.arguments?.getInt(ARG_ID),
                            entry.arguments?.getLong(ARG_DATE),
                            entry.arguments?.getString(ARG_ROLE),
                        )
                    },
                    onDismissRequest = { navHostController.popBackStack() }
                )
            }
        }
        with(Screen.RoleDialog) {
            dialog(
                route = "$route?$ARG_NAME={$ARG_NAME}",
                arguments = listOf(
                    navArgument(ARG_NAME) {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) { entry ->
                RoleDialog(
                    onDismissRequest = { navHostController.popBackStack() },
                    viewModel = koinViewModel { parametersOf(entry.arguments?.getString(ARG_NAME)) }
                )
            }
        }
        dialog(route = Screen.RolePickDialog.route) {
            RolePickDialog(
                viewModel = koinViewModel(),
                onRolePicked = {},
                onDismissRequest = { navHostController.popBackStack() },
                onAddRoleClick = { navHostController.navigate(Screen.RoleDialog.route) }
            )
        }
    }
}