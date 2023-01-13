package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetGoalsForDatesUseCase(private val goalRepository: GoalRepository) {
    fun execute(startDate: LocalDate, endDate: LocalDate): Flow<List<Goal>> {
        return goalRepository.getGoals(startDate, endDate)
    }
}