package com.example.greatweek.data.repository

import com.example.greatweek.data.db.GoalDao
import com.example.greatweek.data.model.Goals
import com.example.greatweek.data.model.toDomain
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class GoalRepositoryImpl(private val goalDao: GoalDao) : GoalRepository {

    override val allGoals = goalDao.getAll().map { goals ->
        goals.map { it.toDomain() }
    }

    override fun getGoals(firstDay: LocalDate, lastDay: LocalDate): Flow<List<Goal>> {
        return goalDao.getGoals(firstDay, lastDay).map { goals ->
            goals.map { it.toDomain() }
        }
    }

    override suspend fun getGoal(goalId: Int): Goal {
        return goalDao.getGoalById(goalId = goalId).toDomain()
    }

    override suspend fun addGoal(goal: Goal) {
        goalDao.addGoal(mapToData(goal, false))
    }

    override suspend fun completeGoal(goalId: Int) {
        goalDao.completeGoal(goalId = goalId)
    }

    override suspend fun editGoal(goal: Goal) {
        goalDao.updateGoal(mapToData(goal, true))
    }

    private fun mapToData(goal: Goal, mapId: Boolean): Goals {
        return if (mapId) Goals(
            id = goal.id,
            title = goal.title,
            description = goal.description,
            role = goal.role,
            date = goal.date,
            time = goal.time,
            commitment = goal.commitment
        ) else Goals(
            title = goal.title,
            description = goal.description,
            role = goal.role,
            date = goal.date,
            time = goal.time,
            commitment = goal.commitment
        )
    }
}