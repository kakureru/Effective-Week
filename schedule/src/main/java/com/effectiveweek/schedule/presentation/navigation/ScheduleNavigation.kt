package com.effectiveweek.schedule.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.effectiveweek.schedule.presentation.ScheduleWithRolesTabScreen
import com.effectiveweek.schedule.presentation.goal_dialog.GoalDialogNavigation
import com.effectiveweek.schedule.presentation.goal_dialog.ui.GoalDialog
import com.effectiveweek.schedule.presentation.role_dialog.ui.RoleDialog
import com.effectiveweek.schedule.presentation.roles_tab.RolesNavigation
import com.effectiveweek.schedule.presentation.schedule.ScheduleNavigation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ScheduleNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ScheduleRoute.Schedule.route) {
        scheduleWithRolesTab(navController = navController)
        goalDialog(navController = navController)
        roleDialog(navController = navController)
    }
}

internal fun NavGraphBuilder.scheduleWithRolesTab(navController: NavHostController) {
    val scheduleNavigation = object : ScheduleNavigation {
        override fun openGoalDialog(goalId: Int) {
            with(ScheduleRoute.GoalDialog) {
                navController.navigate("$route?$ARG_ID=$goalId")
            }
        }

        override fun openGoalDialog(epochDay: Long) {
            with(ScheduleRoute.GoalDialog) {
                navController.navigate("$route?$ARG_DATE=$epochDay")
            }
        }
    }
    val rolesNavigation = object : RolesNavigation {
        override fun openRoleDialog() {
            navController.navigate(ScheduleRoute.RoleDialog.route)
        }

        override fun openRoleDialog(roleName: String) {
            with(ScheduleRoute.RoleDialog) {
                navController.navigate("$route?$ARG_NAME=$roleName")
            }
        }

        override fun openGoalDialog(roleName: String) {
            with(ScheduleRoute.GoalDialog) {
                navController.navigate("$route?$ARG_ROLE=$roleName")
            }
        }

        override fun openGoalDialog(goalId: Int) {
            with(ScheduleRoute.GoalDialog) {
                navController.navigate("$route?$ARG_ID=$goalId")
            }
        }
    }
    composable(route = ScheduleRoute.Schedule.route) {
        ScheduleWithRolesTabScreen(
            scheduleNavigation = scheduleNavigation,
            rolesNavigation = rolesNavigation
        )
    }
}

internal fun NavGraphBuilder.goalDialog(navController: NavHostController) {
    val navigation = object : GoalDialogNavigation {
        override fun dismiss() {
            navController.popBackStack()
        }

        override fun openRoleDialog() {
            navController.navigate(ScheduleRoute.RoleDialog.route)
        }
    }
    with(ScheduleRoute.GoalDialog) {
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
            ),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        ) { entry ->
            Box(modifier = Modifier.fillMaxSize()) { // to fix bug with weird animation when dialog appear off screen
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
}

internal fun NavGraphBuilder.roleDialog(navController: NavHostController) {
    with(ScheduleRoute.RoleDialog) {
        dialog(
            route = "$route?$ARG_NAME={$ARG_NAME}",
            arguments = listOf(
                navArgument(ARG_NAME) {
                    type = NavType.StringType
                    nullable = true
                }
            ),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        ) { entry ->
            Box(modifier = Modifier.fillMaxSize()) { // to fix bug with weird animation when dialog appear off screen
                RoleDialog(
                    onDismissRequest = { navController.popBackStack() },
                    viewModel = koinViewModel { parametersOf(entry.arguments?.getString(ARG_NAME)) }
                )
            }
        }
    }
}