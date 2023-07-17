package com.example.greatweek.app.presentation.screens.schedule

import java.time.LocalDate

interface ScheduleCallback {
    fun onAddGoalClick(date: LocalDate)
    fun onGoalDrop(goalId: Int, date: LocalDate, isCommitment: Boolean)
}