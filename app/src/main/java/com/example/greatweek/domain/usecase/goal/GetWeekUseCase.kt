package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.Constants
import com.example.greatweek.domain.model.WeekDay
import com.example.greatweek.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek

class GetWeekUseCase(
    private val goalRepository: GoalRepository
) {
    fun execute(): Flow<List<WeekDay>> {
        return goalRepository.allGoals.map { goals ->
            for ((i, v) in DayOfWeek.values().withIndex()) {
                Constants.week[i].goals = goals.filter { it.date?.dayOfWeek == v }
            }
            Constants.week
        }
    }
}