package com.example.greatweek.data.repository

import com.example.greatweek.data.storage.GoalDao
import com.example.greatweek.data.storage.model.Goals
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoalRepositoryImpl(private val goalDao: GoalDao) : GoalRepository {
    override fun getGoalsByDay(dayId: Int): Flow<List<Goal>> {
        return goalDao.getGoalsByDay(dayId = dayId).map { inGoals ->
            inGoals.map { goal ->
                Goal(
                    goal.title,
                    goal.description,
                    goal.roleId,
                    goal.day,
                    goal.type
                )
            }
        }
    }
}