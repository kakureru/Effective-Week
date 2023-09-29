package com.example.greatweek.di

import com.example.greatweek.ui.MainActivity
import com.example.greatweek.ui.screens.goaldialog.GoalDialogFragment
import com.example.greatweek.ui.screens.goaldialog.rolepicker.RolePickerDialogFragment
import com.example.greatweek.ui.screens.roledialog.RoleDialogFragment
import com.example.greatweek.ui.screens.roles.RolesFragment
import com.example.greatweek.ui.screens.schedule.ScheduleFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DomainModule::class, DataModule::class, NavigationModule::class])
interface AppComponent {

    // Activities
    fun inject(activity: MainActivity)

    // Fragments
    fun inject(fragment: GoalDialogFragment)
    fun inject(fragment: RoleDialogFragment)
    fun inject(fragment: RolePickerDialogFragment)
    fun inject(fragment: ScheduleFragment)
    fun inject(fragment: RolesFragment)
}