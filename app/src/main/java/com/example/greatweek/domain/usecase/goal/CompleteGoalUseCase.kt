package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.repository.GoalRepository

class CompleteGoalUseCase(private val goalRepository: GoalRepository) {
    suspend fun execute(goalId: Int) {
        goalRepository.completeGoal(goalId = goalId)
    }
}