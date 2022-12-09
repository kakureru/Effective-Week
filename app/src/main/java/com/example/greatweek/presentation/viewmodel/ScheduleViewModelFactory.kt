package com.example.greatweek.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greatweek.domain.usecase.GetWeekUseCase
import com.example.greatweek.domain.usecase.role.AddRoleUseCase

@Suppress("UNCHECKED_CAST")
class ScheduleViewModelFactory(
    private val getWeekUseCase: GetWeekUseCase,
    private val addRoleUseCase: AddRoleUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(
                getWeekUseCase,
                addRoleUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}