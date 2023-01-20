package com.example.greatweek.app.di

import com.example.greatweek.app.presentation.view.GoalDialogFragment
import com.example.greatweek.app.presentation.view.MainActivity
import com.example.greatweek.app.presentation.view.RoleDialogFragment
import com.example.greatweek.app.presentation.view.SettingsActivity
import com.example.greatweek.app.presentation.viewmodel.SettingsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DomainModule::class, DataModule::class, NetworkModule::class])
interface AppComponent {

    // Activities
    fun inject(activity: MainActivity)
    fun inject(activity: SettingsActivity)

    // Fragments
    fun inject(fragment: GoalDialogFragment)
    fun inject(fragment: RoleDialogFragment)
}