package com.example.greatweek.app.di

import com.example.greatweek.app.presentation.MainActivity
import com.example.greatweek.app.presentation.screens.goaldialog.GoalDialogFragment
import com.example.greatweek.app.presentation.screens.roledialog.RoleDialogFragment
import com.example.greatweek.app.presentation.screens.schedule.ScheduleFragment
import com.example.greatweek.app.presentation.screens.settings.SettingsActivity
import com.example.greatweek.app.presentation.screens.settings.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DomainModule::class, DataModule::class, NetworkModule::class, NavigationModule::class])
interface AppComponent {

    // Activities
    fun inject(activity: MainActivity)
    fun inject(activity: SettingsActivity)

    // Fragments
    fun inject(fragment: GoalDialogFragment)
    fun inject(fragment: RoleDialogFragment)
    fun inject(fragment: ScheduleFragment)
    fun inject(fragment: SettingsFragment)
}