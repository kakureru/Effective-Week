package com.example.greatweek.di

import com.example.greatweek.data.db.AppDatabase
import com.example.schedule.data.db.GoalDao
import com.example.schedule.data.db.RoleDao
import com.example.schedule.data.repository.GoalRepositoryImpl
import com.example.schedule.data.repository.RoleRepositoryImpl
import com.example.schedule.domain.repository.GoalRepository
import com.example.schedule.domain.repository.RoleRepository
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