package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository

class AddGoalUseCase(private val goalRepository: GoalRepository) {
    fun execute(goal: Goal) {
        goalRepository.addGoal(goal = goal)
    }
}