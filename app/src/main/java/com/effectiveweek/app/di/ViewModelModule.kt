package com.effectiveweek.app.di

import com.effectiveweek.schedule.presentation.goal_dialog.GoalDialogViewModel
import com.effectiveweek.schedule.presentation.role_dialog.RoleDialogViewModel
import com.effectiveweek.schedule.presentation.roles_tab.RolesViewModel
import com.effectiveweek.schedule.presentation.schedule.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        ScheduleViewModel(
            dropGoalToWeekUseCase = get(),
            getScheduleForDatesUseCase = get(),
            completeGoalUseCase = get(),
        )
    }

    viewModel {
        RolesViewModel(
            roleRepository = get(),
            getRolesWithGoalsUseCase = get(),
            dropGoalToRoleUseCase = get(),
            completeGoalUseCase = get(),
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