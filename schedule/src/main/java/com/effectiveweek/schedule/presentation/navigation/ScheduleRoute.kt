package com.effectiveweek.schedule.presentation.navigation

internal sealed class ScheduleRoute(val route: String) {
    object Schedule : ScheduleRoute(route = "schedule")
    object GoalDialog : ScheduleRoute(route = "goal_dialog") {
        const val ARG_ID = "ARG_ID"
        const val ARG_DATE = "ARG_DATE"
        const val ARG_ROLE = "ARG_ROLE"
    }
    object RoleDialog : ScheduleRoute(route = "role_dialog") {
        const val ARG_NAME = "ARG_NAME"
    }
}