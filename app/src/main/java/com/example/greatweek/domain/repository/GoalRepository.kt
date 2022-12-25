package com.example.greatweek.domain.repository

import com.example.greatweek.domain.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface GoalRepository {

    val allGoals: Flow<List<Goal>>

    suspend fun getGoal(goalId: Int): Goal

    suspend fun addGoal(goal: Goal)

    suspend fun completeGoal(goalId: Int)

    suspend fun editGoal(goal: Goal)
}