package com.example.greatweek.app.di

import com.example.greatweek.app.presentation.viewmodel.GoalDialogFragmentViewModel
import com.example.greatweek.app.presentation.viewmodel.RoleDialogFragmentViewModel
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<ScheduleViewModel> {
        ScheduleViewModel(
            getScheduleUseCase = get(),
            completeGoalUseCase = get(),
            dropGoalToWeekUseCase = get(),
            deleteRoleUseCase = get(),
            getRolesWithGoalsUseCase = get(),
            dropGoalToRoleUseCase = get()
        )
    }

    viewModel<RoleDialogFragmentViewModel> {
        RoleDialogFragmentViewModel(
            addRoleUseCase = get(),
            renameRoleUseCase = get()
        )
    }

    viewModel<GoalDialogFragmentViewModel> {
        GoalDialogFragmentViewModel(
            addGoalUseCase = get(),
            getRolesUseCase = get(),
            getGoalUseCase = get(),
            editGoalUseCase = get()
        )
    }

}