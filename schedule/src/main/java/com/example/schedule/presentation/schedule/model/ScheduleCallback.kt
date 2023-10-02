package com.example.schedule.presentation.schedule.model

import java.time.LocalDate

interface ScheduleCallback {
    fun onAddGoalClick(date: LocalDate)
    fun onGoalDrop(goalId: Int, date: LocalDate, isCommitment: Boolean)
}