package com.example.greatweek.ui.screens.schedule.model

import java.time.LocalDate

interface ScheduleCallback {
    fun onAddGoalClick(date: LocalDate)
    fun onGoalDrop(goalId: Int, date: LocalDate, isCommitment: Boolean)
}