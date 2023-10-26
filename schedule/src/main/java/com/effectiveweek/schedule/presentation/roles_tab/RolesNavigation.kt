package com.effectiveweek.schedule.presentation.roles_tab

internal interface RolesNavigation {
    fun openRoleDialog()
    fun openRoleDialog(roleName: String)
    fun openGoalDialog(goalId: Int)
    fun openGoalDialog(roleName: String)
}