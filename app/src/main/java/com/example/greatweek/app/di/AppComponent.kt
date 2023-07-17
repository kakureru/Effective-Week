package com.example.greatweek.app.di

import com.example.greatweek.app.presentation.MainActivity
import com.example.greatweek.app.presentation.screens.goaldialog.GoalDialogFragment
import com.example.greatweek.app.presentation.screens.roledialog.RoleDialogFragment
import com.example.greatweek.app.presentation.screens.roles.RolesFragment
import com.example.greatweek.app.presentation.screens.schedule.ScheduleFragment
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
    fun inject(fragment: ScheduleFragment)
    fun inject(fragment: RolesFragment)
}