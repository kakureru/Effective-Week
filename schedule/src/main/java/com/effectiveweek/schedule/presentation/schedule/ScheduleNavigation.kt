package com.effectiveweek.schedule.presentation.schedule

internal interface ScheduleNavigation {
    fun openGoalDialog(goalId: Int)
    fun openGoalDialog(epochDay: Long)
}