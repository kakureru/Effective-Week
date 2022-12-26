package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository

class DropGoalToRoleUseCase(private val goalRepository: GoalRepository) {
    suspend fun execute(goalId: Int, roleId: Int) {
        val goal = goalRepository.getGoal(goalId = goalId)
        goalRepository.editGoal(
            Goal(
                id = goal.id,
                title = goal.title,
                description = goal.description,
                roleId = roleId,
                weekday = 0,
                commitment = goal.commitment
            )
        )
    }
}