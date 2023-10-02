package com.example.schedule.di

import com.example.schedule.di.deps.ScheduleDeps
import com.example.schedule.di.modules.DataModule
import com.example.schedule.di.modules.DomainModule
import com.example.schedule.di.modules.ViewModelModule
import com.example.schedule.presentation.goal_dialog.GoalDialogFragment
import com.example.schedule.presentation.role_dialog.RoleDialogFragment
import com.example.schedule.presentation.role_pick_dialog.RolePickerDialogFragment
import com.example.schedule.presentation.schedule.ScheduleFragment
import dagger.Component

@ScheduleScope
@Component(
    dependencies = [ScheduleDeps::class],
    modules = [ViewModelModule::class, DomainModule::class, DataModule::class]
)
internal interface ScheduleComponent {
    fun inject(fragment: GoalDialogFragment)
    fun inject(fragment: RoleDialogFragment)
    fun inject(fragment: RolePickerDialogFragment)
    fun inject(fragment: ScheduleFragment)

    @Component.Factory
    interface Factory {
        fun create(deps: ScheduleDeps): ScheduleComponent
    }
}