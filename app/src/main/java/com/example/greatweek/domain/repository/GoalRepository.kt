package com.example.greatweek.domain.repository

import com.example.greatweek.domain.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface GoalRepository {
    fun getGoalsByDay(dayId: Int): Flow<List<Goal>>

    fun getGoals(): Flow<List<Goal>>

    fun addGoal(goal: Goal)
}