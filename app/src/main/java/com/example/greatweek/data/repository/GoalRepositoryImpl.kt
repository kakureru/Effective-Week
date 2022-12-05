package com.example.greatweek.data.repository

import com.example.greatweek.data.storage.GoalDao
import com.example.greatweek.data.storage.model.Goals
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository

class GoalRepositoryImpl(private val goalDao: GoalDao) : GoalRepository {
    override fun getGoalsByDay(dayId: Int): MutableList<Goal> {
        val goals = goalDao.getGoalsByDay(dayId = dayId)
        return mapToGoal(goals)
    }

    private fun mapToGoal(inGoals: List<Goals>): MutableList<Goal> {
        val outGoals = mutableListOf<Goal>()
        for (goal in inGoals) {
            outGoals.add(Goal(
                goal.title,
            goal.description,
            goal.roleId,
            goal.day,
            goal.type))
        }
        return outGoals
    }
}