package com.example.schedule.domain.repository

import com.example.schedule.domain.model.Goal
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GoalRepository {

    fun getGoals(): Flow<List<Goal>>

    fun getGoals(firstDay: LocalDate, lastDay: LocalDate): Flow<List<Goal>>

    suspend fun getGoal(goalId: Int): Goal

    suspend fun addGoal(goal: Goal)

    suspend fun completeGoal(goalId: Int)

    suspend fun editGoal(goal: Goal)
}