package com.example.greatweek.di

import com.example.greatweek.MainActivity
import com.example.schedule.data.db.GoalDao
import com.example.schedule.data.db.RoleDao
import com.example.schedule.di.deps.ScheduleDeps
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, NavigationModule::class])
interface AppComponent : ScheduleDeps {

    fun inject(activity: MainActivity)

    override fun goalDao(): GoalDao
    override fun roleDao(): RoleDao
}