package com.effectiveweek.schedule.presentation.schedule.model

import com.effectiveweek.schedule.domain.model.ScheduleDay
import com.effectiveweek.schedule.presentation.model.GoalItem
import com.effectiveweek.schedule.presentation.model.toGoalItem
import java.time.LocalDate

class ScheduleDayModel(
    val date: LocalDate,
    val weekday: String,
    val dateNumber: String,
    val isToday: Boolean,
    val priorities: List<GoalItem>,
    val appointments: List<GoalItem>,
)

fun ScheduleDay.toScheduleDayItem() = ScheduleDayModel(
    date = date,
    weekday = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
    dateNumber = date.dayOfMonth.toString(),
    isToday = date == LocalDate.now(),
    priorities = goals.filter { !it.appointment }.map { it.toGoalItem() },
    appointments = goals.filter { it.appointment }.map { it.toGoalItem() },
)