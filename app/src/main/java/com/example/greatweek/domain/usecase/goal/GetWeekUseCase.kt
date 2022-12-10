package com.example.greatweek.domain.usecase.goal

import com.example.greatweek.domain.model.WeekDay
import com.example.greatweek.domain.repository.GoalRepository

class GetWeekUseCase(
    private val goalRepository: GoalRepository) {
    fun execute(): List<WeekDay> {
        return listOf(
            WeekDay(1, "Sunday", goalRepository.getGoalsByDay(1)),
            WeekDay(2, "Monday", goalRepository.getGoalsByDay(2)),
            WeekDay(3, "Tuesday ", goalRepository.getGoalsByDay(3)),
            WeekDay(4, "Wednesday", goalRepository.getGoalsByDay(4)),
            WeekDay(5, "Thursday", goalRepository.getGoalsByDay(5)),
            WeekDay(6, "Friday", goalRepository.getGoalsByDay(6)),
            WeekDay(7, "Saturday", goalRepository.getGoalsByDay(7))
        )
    }
}