package com.example.greatweek.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greatweek.domain.usecase.GetWeekUseCase

@Suppress("UNCHECKED_CAST")
class ScheduleViewModelFactory(
    private val getWeekUseCase: GetWeekUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(getWeekUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}