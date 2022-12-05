package com.example.greatweek.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.greatweek.domain.usecase.GetWeekUseCase

class ScheduleViewModel(
    private val getWeekUseCase: GetWeekUseCase
): ViewModel() {
    val week = getWeekUseCase.execute()
}