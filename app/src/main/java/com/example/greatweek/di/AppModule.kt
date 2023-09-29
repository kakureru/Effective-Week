package com.example.greatweek.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.greatweek.ui.screens.goaldialog.rolepicker.RolePickerViewModel
import com.example.greatweek.ui.screens.roledialog.RoleDialogFragmentViewModel
import com.example.greatweek.ui.screens.schedule.ScheduleViewModel
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
        @ViewModelKey(RoleDialogFragmentViewModel::class)
        fun bindsRoleDialogViewModel(viewModel: RoleDialogFragmentViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(RolePickerViewModel::class)
        fun bindsRolePickerViewModel(viewModel: RolePickerViewModel): ViewModel
    }
}