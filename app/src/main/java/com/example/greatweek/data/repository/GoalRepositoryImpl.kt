package com.example.greatweek.data.repository

import com.example.greatweek.data.db.GoalDao
import com.example.greatweek.data.db.model.GoalEntity
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class GoalRepositoryImpl(
    private val goalDao: GoalDao,
) : GoalRepository {

    override fun getGoals() = goalDao.getAll().map { goals ->
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

    private fun mapToData(goal: Goal, mapId: Boolean): GoalEntity {
        return if (mapId) GoalEntity(
            id = goal.id,
            title = goal.title,
            description = goal.description,
            role = goal.role,
            date = goal.date,
            time = goal.time,
            commitment = goal.commitment
        ) else GoalEntity(
            title = goal.title,
            description = goal.description,
            role = goal.role,
            date = goal.date,
            time = goal.time,
            commitment = goal.commitment
        )
    }

    private fun GoalEntity.toDomain() = Goal(
        id = id,
        title = title,
        description = description,
        role = role,
        date = date,
        time = time,
        commitment = commitment
    )
}