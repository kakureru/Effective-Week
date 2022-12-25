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
            mapToDomain(goals = inGoals)
        }
    }

    override fun getGoal(goalId: Int): Goal {
        return mapToDomain(goalDao.getGoalById(goalId = goalId))
    }

    override fun addGoal(goal: Goal) {
        goalDao.addGoal(
            Goals(
                title = goal.title,
                description = goal.description,
                roleId = goal.roleId,
                day = goal.weekday,
                commitment = goal.commitment
            )
        )
    }

    override fun completeGoal(goalId: Int) {
        goalDao.completeGoal(goalId = goalId)
    }

    override fun editGoal(goal: Goal) {
        goalDao.updateGoal(
            mapToData(goal)
        )
    }

    private fun mapToDomain(goals: List<Goals>): List<Goal> {
        return goals.map { goal ->
            mapToDomain(goal)
        }
    }

    private fun mapToDomain(goal: Goals): Goal {
        return Goal(
            id = goal.id,
            title = goal.title,
            description = goal.description,
            roleId = goal.roleId,
            weekday = goal.day,
            commitment = goal.commitment
        )
    }

    private fun mapToData(goal: Goal): Goals {
        return Goals(
            id = goal.id,
            title = goal.title,
            description = goal.description,
            roleId = goal.roleId,
            day = goal.weekday,
            commitment = goal.commitment
        )
    }
}