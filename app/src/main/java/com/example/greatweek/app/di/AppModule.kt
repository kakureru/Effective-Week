package com.example.greatweek.app.di

import android.content.Context
import android.content.SharedPreferences
import com.example.greatweek.R
import com.example.greatweek.app.presentation.viewmodel.GoalDialogFragmentViewModelFactory
import com.example.greatweek.app.presentation.viewmodel.RoleDialogFragmentViewModelFactory
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModelFactory
import com.example.greatweek.app.presentation.viewmodel.SettingsViewModelFactory
import com.example.greatweek.data.network.GreatWeekApi
import com.example.greatweek.domain.usecase.goal.*
import com.example.greatweek.domain.usecase.role.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
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
        return ScheduleViewModelFactory(
            getScheduleUseCase = getScheduleUseCase,
            completeGoalUseCase = completeGoalUseCase,
            dropGoalToWeekUseCase = dropGoalToWeekUseCase,
            deleteRoleUseCase = deleteRoleUseCase,
            getRolesWithGoalsUseCase = getRolesWithGoalsUseCase,
            dropGoalToRoleUseCase = dropGoalToRoleUseCase
        )
    }

    @Provides
    fun provideSettingsViewModelFactory(
        greatWeekApi: GreatWeekApi,
        sharedPreferences: SharedPreferences
    ): SettingsViewModelFactory {
        return SettingsViewModelFactory(
            greatWeekApi = greatWeekApi,
            sharedPreferences = sharedPreferences
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