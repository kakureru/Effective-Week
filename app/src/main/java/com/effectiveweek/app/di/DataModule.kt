package com.effectiveweek.app.di

import com.effectiveweek.app.data.db.AppDatabase
import com.effectiveweek.schedule.data.db.GoalDao
import com.effectiveweek.schedule.data.db.RoleDao
import com.effectiveweek.schedule.data.repository.GoalRepositoryImpl
import com.effectiveweek.schedule.data.repository.RoleRepositoryImpl
import com.effectiveweek.schedule.domain.repository.GoalRepository
import com.effectiveweek.schedule.domain.repository.RoleRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    single<GoalDao> {
        AppDatabase.getInstance(application = androidApplication()).GoalDao()
    }

    single<RoleDao> {
        AppDatabase.getInstance(application = androidApplication()).RoleDao()
    }

    single<GoalRepository> {
        GoalRepositoryImpl(goalDao = get())
    }

    single<RoleRepository> {
        RoleRepositoryImpl(roleDao = get())
    }
}