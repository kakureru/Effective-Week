package com.example.schedule.presentation.schedule.model

import com.example.schedule.domain.model.ScheduleDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduleDayItem(
    val date: LocalDate,
    val weekday: String,
    val dateText: String,
    val isToday: Boolean,
    val priorities: List<GoalItem>,
    val appointments: List<GoalItem>,
)

fun ScheduleDay.toScheduleDayItem() = ScheduleDayItem(
    date = date,
    weekday = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
    dateText = DateTimeFormatter.ofPattern("MMM d").format(date).replaceFirstChar { it.uppercase() },
    isToday = date == LocalDate.now(),
    priorities = goals.filter { !it.appointment }.map { it.toGoalItem() },
    appointments = goals.filter { it.appointment }.map { it.toGoalItem() },
)