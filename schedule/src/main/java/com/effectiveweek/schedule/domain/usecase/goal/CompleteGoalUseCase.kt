package com.effectiveweek.schedule.domain.usecase.goal

import com.effectiveweek.schedule.domain.repository.GoalRepository

internal class CompleteGoalUseCase(
    private val goalRepository: GoalRepository,
) {
    suspend operator fun invoke(goalId: Int) {
        goalRepository.completeGoal(goalId = goalId)
    }
}