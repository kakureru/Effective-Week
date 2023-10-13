package com.effectiveweek.schedule.presentation.schedule

interface ScheduleNavigation {
    fun openGoalDialog(goalId: Int)
    fun openGoalDialog(epochDay: Long)
}