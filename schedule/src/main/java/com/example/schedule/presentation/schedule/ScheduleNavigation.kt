package com.example.schedule.presentation.schedule

interface ScheduleNavigation {
    fun openRoleDialog()
    fun openRoleDialog(roleName: String)
    fun openGoalDialog(goalId: Int)
    fun openGoalDialog(epochDay: Long)
    fun openGoalDialog(roleName: String)
}