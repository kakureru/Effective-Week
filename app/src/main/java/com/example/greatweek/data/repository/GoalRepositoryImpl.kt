package com.example.greatweek.data.repository

import com.example.greatweek.data.storage.GoalDao
import com.example.greatweek.data.storage.model.Goals
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class GoalRepositoryImpl(private val goalDao: GoalDao) : GoalRepository {

    override val allGoals = goalDao.getAll().map {
        mapToDomain(it)
    }

    override fun getGoals(firstDay: LocalDate, lastDay: LocalDate): Flow<List<Goal>> {
        return goalDao.getGoals(firstDay, lastDay).map {
            mapToDomain(it)
        }
    }

    override suspend fun getGoal(goalId: Int): Goal {
        return mapToDomain(goalDao.getGoalById(goalId = goalId))
    }

    override suspend fun addGoal(goal: Goal) {
        goalDao.addGoal(
            Goals(
                title = goal.title,
                description = goal.description,
                role = goal.role,
                date = goal.date,
                time = goal.time,
                commitment = goal.commitment
            )
        )
    }

    override suspend fun completeGoal(goalId: Int) {
        goalDao.completeGoal(goalId = goalId)
    }

    override suspend fun editGoal(goal: Goal) {
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
            role = goal.role,
            date = goal.date,
            time = goal.time,
            commitment = goal.commitment
        )
    }

    private fun mapToData(goal: Goal): Goals {
        return Goals(
            id = goal.id,
            title = goal.title,
            description = goal.description,
            role = goal.role,
            date = goal.date,
            time = goal.time,
            commitment = goal.commitment
        )
    }
}