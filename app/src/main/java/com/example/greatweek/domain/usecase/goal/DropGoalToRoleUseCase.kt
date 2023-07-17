package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.repository.GoalRepository

class DropGoalToRoleUseCase(private val goalRepository: GoalRepository) {
    suspend operator fun invoke(goalId: Int, role: String) {
        val goal = goalRepository.getGoal(goalId = goalId).copy(role = role, date = null)
        goalRepository.editGoal(goal)
    }
}