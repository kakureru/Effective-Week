package com.example.greatweek.domain.usecase

import com.example.greatweek.domain.model.WeekDay
import com.example.greatweek.domain.usecase.goal.GetGoalsByDayUseCase

class GetWeekUseCase(
    private val getGoalsByDayUseCase: GetGoalsByDayUseCase) {
    fun execute(): List<WeekDay> {
        return listOf(
            WeekDay(1, "Sunday", getGoalsByDayUseCase.execute(1)),
            WeekDay(2, "Monday", getGoalsByDayUseCase.execute(2)),
            WeekDay(3, "Tuesday ", getGoalsByDayUseCase.execute(3)),
            WeekDay(4, "Wednesday", getGoalsByDayUseCase.execute(4)),
            WeekDay(5, "Thursday", getGoalsByDayUseCase.execute(5)),
            WeekDay(6, "Friday", getGoalsByDayUseCase.execute(6)),
            WeekDay(7, "Saturday", getGoalsByDayUseCase.execute(7))
        )
    }
}