package com.effectiveweek.app.di

import com.effectiveweek.app.data.db.AppDatabase
import com.effectiveweek.schedule.data.db.GoalDao
import com.effectiveweek.schedule.data.db.RoleDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    single<GoalDao> {
        AppDatabase.getInstance(application = androidApplication()).GoalDao()
    }

    single<RoleDao> {
        AppDatabase.getInstance(application = androidApplication()).RoleDao()
    }
}