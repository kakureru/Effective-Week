package com.example.greatweek.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.example.schedule.presentation.goal_dialog.GoalDialogNavigation
import com.example.schedule.presentation.goal_dialog.ui.GoalDialog
import com.example.schedule.presentation.role_dialog.ui.RoleDialog
import com.example.schedule.presentation.role_pick_dialog.ui.RolePickDialog
import com.example.schedule.presentation.schedule.ScheduleNavigation
import com.example.schedule.presentation.schedule.ui.ScheduleScreen
import com.example.schedule.presentation.schedule.ui.previewGoalCallback
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RootNavGraph(navHostController: NavHostController, startDestination: String) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        schedule(navController = navHostController)
        goalDialog(navController = navHostController)
        roleDialog(navController = navHostController)
    }
}

private fun NavGraphBuilder.schedule(navController: NavHostController) {
    val scheduleNavigation = object : ScheduleNavigation {
        override fun openRoleDialog() {
            navController.navigate(Screen.RoleDialog.route)
        }

        override fun openRoleDialog(roleName: String) {
            with(Screen.RoleDialog) {
                navController.navigate("$route?$ARG_NAME=$roleName")
            }
        }

        override fun openGoalDialog(goalId: Int) {
            with(Screen.GoalDialog) {
                navController.navigate("$route?$ARG_ID=$goalId")
            }
        }

        override fun openGoalDialog(epochDay: Long) {
            with(Screen.GoalDialog) {
                navController.navigate("$route?$ARG_DATE=$epochDay")
            }
        }

        override fun openGoalDialog(roleName: String) {
            with(Screen.GoalDialog) {
                navController.navigate("$route?$ARG_ROLE=$roleName")
            }
        }
    }
    composable(route = Screen.Schedule.route) {
        ScheduleScreen(
            navigation = scheduleNavigation,
            viewModel = koinViewModel(),
        )
    }
}

private fun NavGraphBuilder.goalDialog(navController: NavHostController) {
    val navigation = object : GoalDialogNavigation {
        override fun dismiss() { navController.popBackStack() }
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
                navigation = navigation,
                viewModel = koinViewModel {
                    parametersOf(
                        entry.arguments?.getInt(ARG_ID),
                        entry.arguments?.getLong(ARG_DATE),
                        entry.arguments?.getString(ARG_ROLE),
                    )
                },
            )
        }
    }
}

private fun NavGraphBuilder.roleDialog(navController: NavHostController) {
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
                onDismissRequest = { navController.popBackStack() },
                viewModel = koinViewModel { parametersOf(entry.arguments?.getString(ARG_NAME)) }
            )
        }
    }
}