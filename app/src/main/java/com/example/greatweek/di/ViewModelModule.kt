package com.example.greatweek.di

import com.example.schedule.presentation.goal_dialog.GoalDialogViewModel
import com.example.schedule.presentation.role_dialog.RoleDialogViewModel
import com.example.schedule.presentation.schedule.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        ScheduleViewModel(
            goalRepository = get(),
            roleRepository = get(),
            dropGoalToWeekUseCase = get(),
            dropGoalToRoleUseCase = get(),
            getRolesWithGoalsUseCase = get(),
            getScheduleForDatesUseCase = get(),
        )
    }

    viewModel { params ->
        GoalDialogViewModel(
            goalId = params[0],
            initDateEpochDay = params[1],
            initRole = params[2],
            goalRepository = get(),
            roleRepository = get(),
        )
    }

    viewModel { params ->
        RoleDialogViewModel(
            roleName = params[0],
            roleRepository = get(),
        )
    }
}