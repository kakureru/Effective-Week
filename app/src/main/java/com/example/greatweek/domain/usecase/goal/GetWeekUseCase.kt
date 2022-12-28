package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.Constants
import com.example.greatweek.domain.model.WeekDay
import com.example.greatweek.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWeekUseCase(
    private val goalRepository: GoalRepository
) {
    fun execute(): Flow<List<WeekDay>> {
        return goalRepository.allGoals.map { goals ->
            for (i in Constants.week.indices) {
                Constants.week[i].goals = goals.filter { it.weekday == i + 1 }
            }
            Constants.week
        }
    }
}