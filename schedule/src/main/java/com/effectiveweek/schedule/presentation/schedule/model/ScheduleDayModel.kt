package com.effectiveweek.schedule.presentation.schedule.model

import com.effectiveweek.schedule.domain.model.ScheduleDay
import com.effectiveweek.schedule.presentation.goal_item.model.GoalItem
import com.effectiveweek.schedule.presentation.goal_item.model.toGoalItem
import java.time.LocalDate

internal class ScheduleDayModel(
    val date: LocalDate,
    val weekday: String,
    val dateNumber: String,
    val isToday: Boolean,
    val priorities: List<GoalItem>,
    val appointments: List<GoalItem>,
)

internal fun ScheduleDay.toScheduleDayItem() = ScheduleDayModel(
    date = date,
    weekday = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
    dateNumber = date.dayOfMonth.toString(),
    isToday = date == LocalDate.now(),
    priorities = goals.filter { !it.appointment }.map { it.toGoalItem() },
    appointments = goals.filter { it.appointment }.map { it.toGoalItem() },
)