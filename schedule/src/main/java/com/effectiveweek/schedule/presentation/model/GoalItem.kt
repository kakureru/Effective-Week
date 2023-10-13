package com.effectiveweek.schedule.presentation.model

import com.effectiveweek.schedule.domain.model.Goal

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