package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository

class GetGoalUseCase(private val goalRepository: GoalRepository) {
    fun execute(goalId: Int): Goal {
        return goalRepository.getGoal(goalId = goalId)
    }
}