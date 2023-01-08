package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.model.WeekDay
import com.example.greatweek.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class GetWeekUseCase(
    private val goalRepository: GoalRepository
) {
    fun execute(startDate: LocalDate, endDate: LocalDate): Flow<List<WeekDay>> {
        return goalRepository.getWeekGoals(startDate, endDate).map { goals ->
            val week = mutableListOf<WeekDay>()
            for (i in 0..6) {
                val date = startDate.plusDays(i.toLong())
                week.add(
                    WeekDay(
                        date = date,
                        goals = goals.filter { it.date == date })
                )
            }
            week
        }
    }
}