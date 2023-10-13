package com.example.greatweek.navigation

sealed class Screen(val route: String) {
    object Schedule : Screen(route = "schedule")
    object GoalDialog : Screen(route = "goal_dialog") {
        const val ARG_ID = "ARG_ID"
        const val ARG_DATE = "ARG_DATE"
        const val ARG_ROLE = "ARG_ROLE"
    }
    object RoleDialog : Screen(route = "role_dialog") {
        const val ARG_NAME = "ARG_NAME"
    }
}