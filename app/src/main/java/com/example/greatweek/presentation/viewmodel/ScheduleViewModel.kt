package com.example.greatweek.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.greatweek.domain.model.WeekDay
import com.example.greatweek.domain.usecase.goal.GetWeekUseCase
import kotlinx.coroutines.flow.Flow

class ScheduleViewModel(
    private val getWeekUseCase: GetWeekUseCase
): ViewModel() {

    fun getWeek(): Flow<List<WeekDay>> {
        return getWeekUseCase.execute()
    }
}