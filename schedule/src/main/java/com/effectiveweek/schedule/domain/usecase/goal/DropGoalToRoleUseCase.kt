package com.effectiveweek.schedule.domain.usecase.goal

import com.effectiveweek.schedule.domain.repository.GoalRepository

internal class DropGoalToRoleUseCase(private val goalRepository: GoalRepository) {
    suspend operator fun invoke(goalId: Int, role: String) {
        val goal = goalRepository.getGoal(goalId = goalId).copy(role = role, date = null)
        goalRepository.editGoal(goal)
    }
}