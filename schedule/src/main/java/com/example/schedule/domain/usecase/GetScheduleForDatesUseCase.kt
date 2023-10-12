package com.example.schedule.domain.usecase

import com.example.schedule.domain.model.ScheduleDay
import com.example.schedule.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.IntStream

class GetScheduleForDatesUseCase(
    private val goalRepository: GoalRepository,
) {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<List<ScheduleDay>> {
        return goalRepository.getGoals(startDate, endDate).map { goals ->
            IntStream
                .iterate(0) { i -> i + 1 }
                .limit(ChronoUnit.DAYS.between(startDate, endDate))
                .mapToObj { i ->
                    val date = startDate.plusDays(i.toLong())
                    ScheduleDay(
                        date = date,
                        goals = goals.filter { it.date == date }
                    )
                }
                .collect(Collectors.toList())
        }
    }
}