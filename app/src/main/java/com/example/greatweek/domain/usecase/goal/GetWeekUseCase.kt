package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.model.WeekDay
import com.example.greatweek.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWeekUseCase(
    private val goalRepository: GoalRepository
) {
    fun execute(): Flow<List<WeekDay>> {
        return goalRepository.allGoals.map { goals ->
            listOf(
                WeekDay(1, "Sunday",    goals.filter { it.weekday == 1 }),
                WeekDay(2, "Monday",    goals.filter { it.weekday == 2 }),
                WeekDay(3, "Tuesday ",  goals.filter { it.weekday == 3 }),
                WeekDay(4, "Wednesday", goals.filter { it.weekday == 4 }),
                WeekDay(5, "Thursday",  goals.filter { it.weekday == 5 }),
                WeekDay(6, "Friday",    goals.filter { it.weekday == 6 }),
                WeekDay(7, "Saturday",  goals.filter { it.weekday == 7 })
            )
        }
    }
}