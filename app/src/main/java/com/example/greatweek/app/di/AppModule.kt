package com.example.greatweek.app.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.greatweek.app.presentation.screens.goaldialog.GoalDialogFragmentViewModel
import com.example.greatweek.app.presentation.screens.roledialog.RoleDialogFragmentViewModel
import com.example.greatweek.app.presentation.screens.roles.RolesViewModel
import com.example.greatweek.app.presentation.screens.schedule.ScheduleViewModel
import com.example.greatweek.app.presentation.screens.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [AppModule.BindsModule::class])
class AppModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Module
    interface BindsModule {
        @Binds
        @IntoMap
        @ViewModelKey(ScheduleViewModel::class)
        fun bindsScheduleViewModel(viewModel: ScheduleViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(RolesViewModel::class)
        fun bindsRolesViewModel(viewModel: RolesViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(GoalDialogFragmentViewModel::class)
        fun bindsGoalDialogFragmentViewModel(viewModel: GoalDialogFragmentViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(RoleDialogFragmentViewModel::class)
        fun bindsRoleDialogFragmentViewModel(viewModel: RoleDialogFragmentViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(SettingsViewModel::class)
        fun bindsSettingsViewModel(viewModel: SettingsViewModel): ViewModel
    }
}