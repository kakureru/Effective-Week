package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository

class EditGoalUseCase(private val goalRepository: GoalRepository) {
    suspend fun execute(goal: Goal) {
        goalRepository.editGoal(goal = goal)
    }
}