package com.example.greatweek.app.di

import android.content.Context
import com.example.greatweek.app.presentation.viewmodel.*
import com.example.greatweek.domain.usecase.goal.*
import com.example.greatweek.domain.usecase.role.*
import dagger.Module
import dagger.Provides

@Module
class AppModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideScheduleViewModelFactory(
        getScheduleUseCase: GetGoalsForDatesUseCase,
        completeGoalUseCase: CompleteGoalUseCase,
        dropGoalToWeekUseCase: DropGoalToWeekUseCase,
        deleteRoleUseCase: DeleteRoleUseCase,
        getRolesWithGoalsUseCase: GetRolesWithGoalsUseCase,
        dropGoalToRoleUseCase: DropGoalToRoleUseCase
    ): ScheduleViewModelFactory {
        return ScheduleViewModelFactory (
            getScheduleUseCase = getScheduleUseCase,
            completeGoalUseCase = completeGoalUseCase,
            dropGoalToWeekUseCase = dropGoalToWeekUseCase,
            deleteRoleUseCase = deleteRoleUseCase,
            getRolesWithGoalsUseCase = getRolesWithGoalsUseCase,
            dropGoalToRoleUseCase = dropGoalToRoleUseCase
        )
    }

    @Provides
    fun provideRoleDialogFragmentViewModelFactory(
        addRoleUseCase: AddRoleUseCase,
        renameRoleUseCase: RenameRoleUseCase
    ): RoleDialogFragmentViewModelFactory {
        return RoleDialogFragmentViewModelFactory(
            addRoleUseCase = addRoleUseCase,
            renameRoleUseCase = renameRoleUseCase
        )
    }

    @Provides
    fun provideGoalDialogFragmentViewModelFactory(
        addGoalUseCase: AddGoalUseCase,
        getRolesUseCase: GetRolesUseCase,
        editGoalUseCase: EditGoalUseCase,
        getGoalUseCase: GetGoalUseCase
    ): GoalDialogFragmentViewModelFactory {
        return GoalDialogFragmentViewModelFactory(
            addGoalUseCase = addGoalUseCase,
            getRolesUseCase = getRolesUseCase,
            editGoalUseCase = editGoalUseCase,
            getGoalUseCase = getGoalUseCase
        )
    }
}