package com.example.schedule.di.modules

import androidx.lifecycle.ViewModel
import com.example.utils.ViewModelKey
import com.example.schedule.presentation.role_dialog.RoleDialogViewModel
import com.example.schedule.presentation.role_pick_dialog.RolePickerViewModel
import com.example.schedule.presentation.schedule.ScheduleViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal interface ViewModelModule {

    @Binds
    @IntoMap
    @com.example.utils.ViewModelKey(ScheduleViewModel::class)
    fun bindsScheduleViewModel(viewModel: ScheduleViewModel): ViewModel

    @Binds
    @IntoMap
    @com.example.utils.ViewModelKey(RoleDialogViewModel::class)
    fun bindsRoleDialogViewModel(viewModel: RoleDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @com.example.utils.ViewModelKey(RolePickerViewModel::class)
    fun bindsRolePickerViewModel(viewModel: RolePickerViewModel): ViewModel
}