package com.example.greatweek.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.greatweek.domain.model.WeekDay
import com.example.greatweek.domain.usecase.GetWeekUseCase
import com.example.greatweek.domain.usecase.role.AddRoleUseCase

class ScheduleViewModel(
    private val getWeekUseCase: GetWeekUseCase,
    private val addRoleUseCase: AddRoleUseCase
): ViewModel() {

    fun getWeek(): List<WeekDay> {
        return getWeekUseCase.execute()
    }

    fun addRole(name: String) {
        addRoleUseCase.execute(name = name)
    }
}