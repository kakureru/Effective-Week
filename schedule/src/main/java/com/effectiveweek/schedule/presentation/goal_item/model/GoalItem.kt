package com.effectiveweek.schedule.presentation.goal_item.model

import com.effectiveweek.schedule.domain.model.Goal

internal class GoalItem(
    val id: Int,
    val title: String,
    val role: String,
)

internal fun Goal.toGoalItem() = GoalItem(
    id = id,
    title = title,
    role = role.toString()
)