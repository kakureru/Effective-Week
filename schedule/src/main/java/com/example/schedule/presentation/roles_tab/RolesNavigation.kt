package com.example.schedule.presentation.roles_tab

interface RolesNavigation {
    fun openRoleDialog()
    fun openRoleDialog(roleName: String)
    fun openGoalDialog(goalId: Int)
    fun openGoalDialog(roleName: String)
}