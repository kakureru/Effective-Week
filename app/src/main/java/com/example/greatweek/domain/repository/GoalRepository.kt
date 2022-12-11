package com.example.greatweek.domain.repository

import com.example.greatweek.domain.model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getGoalsByDay(dayId: Int): Flow<List<Goal>>
}