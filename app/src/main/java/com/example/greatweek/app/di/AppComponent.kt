package com.example.greatweek.app.di

import com.example.greatweek.app.presentation.view.GoalDialogFragment
import com.example.greatweek.app.presentation.view.MainActivity
import com.example.greatweek.app.presentation.view.RoleDialogFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, DomainModule::class, DataModule::class])
@Singleton
interface AppComponent {

    // Activities
    fun inject(activity: MainActivity)

    // Fragments
    fun inject(activity: GoalDialogFragment)
    fun inject(activity: RoleDialogFragment)
}