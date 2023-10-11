package com.example.schedule.domain.usecase.goal

import com.example.schedule.domain.repository.GoalRepository

class CompleteGoalUseCase(
    private val goalRepository: GoalRepository,
) {
    suspend operator fun invoke(goalId: Int) {
        goalRepository.completeGoal(goalId = goalId)
    }
}