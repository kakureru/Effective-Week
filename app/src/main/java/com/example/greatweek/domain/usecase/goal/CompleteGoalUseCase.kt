package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.data.repository.GoalRepositoryImpl

class CompleteGoalUseCase(private val goalRepository: GoalRepositoryImpl) {
    fun execute(goalId: Int) {
        goalRepository.completeGoal(goalId = goalId)
    }
}