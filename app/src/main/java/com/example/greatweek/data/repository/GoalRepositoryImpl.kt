package com.example.greatweek.data.repository

import com.example.greatweek.data.storage.GoalDao
import com.example.greatweek.data.storage.model.Goals
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoalRepositoryImpl(private val goalDao: GoalDao) : GoalRepository {

    override fun getGoals(): Flow<List<Goal>> {
        return goalDao.getGoals().map { inGoals ->
            mapDataGoal(inGoals = inGoals)
        }
    }

    private fun mapDataGoal(inGoals: List<Goals>): List<Goal> {
        return inGoals.map { goal ->
            Goal(
                goal.id,
                goal.title,
                goal.description,
                goal.roleId,
                goal.day,
                goal.type
            )
        }
    }

    override fun addGoal(goal: Goal) {
        goalDao.addGoal(
            Goals(
                title = goal.title,
                description = goal.description,
                roleId = goal.roleId,
                day = goal.weekday,
                type = goal.type
            )
        )
    }

    override fun completeGoal(goalId: Int) {
        goalDao.completeGoal(goalId = goalId)
    }
}