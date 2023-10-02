package com.example.schedule.presentation.schedule.model

import com.example.schedule.domain.model.Goal

class GoalItem(
    val id: Int,
    val title: String,
    val role: String,
)

fun Goal.toGoalItem() = GoalItem(
    id = id,
    title = title,
    role = role.toString()
)