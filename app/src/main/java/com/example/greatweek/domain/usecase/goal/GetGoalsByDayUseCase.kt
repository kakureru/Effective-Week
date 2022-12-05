package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository

class GetGoalsByDayUseCase(private val goalRepository: GoalRepository) {
    fun execute(dayId: Int): MutableList<Goal> {
        return goalRepository.getGoalsByDayUseCase(dayId = dayId)
    }
}