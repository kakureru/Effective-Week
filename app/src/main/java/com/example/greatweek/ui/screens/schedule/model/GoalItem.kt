package com.example.greatweek.ui.screens.schedule.model

import com.example.greatweek.domain.model.Goal

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