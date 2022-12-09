package com.example.greatweek.domain.repository

import com.example.greatweek.domain.model.Goal

interface GoalRepository {
    fun getGoalsByDay(dayId: Int): MutableList<Goal>
}