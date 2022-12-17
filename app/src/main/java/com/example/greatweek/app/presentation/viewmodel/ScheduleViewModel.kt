package com.example.greatweek.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greatweek.domain.model.WeekDay
import com.example.greatweek.domain.usecase.goal.CompleteGoalUseCase
import com.example.greatweek.domain.usecase.goal.GetWeekUseCase
import kotlinx.coroutines.flow.Flow

class ScheduleViewModel(
    private val getWeekUseCase: GetWeekUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase
): ViewModel() {

    fun getWeek(): Flow<List<WeekDay>> {
        return getWeekUseCase.execute()
    }

    fun completeGoal(goalId: Int) {
        completeGoalUseCase.execute(goalId = goalId)
    }
}