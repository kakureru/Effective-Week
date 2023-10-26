package com.effectiveweek.schedule.di

import com.effectiveweek.schedule.data.repository.GoalRepositoryImpl
import com.effectiveweek.schedule.data.repository.RoleRepositoryImpl
import com.effectiveweek.schedule.domain.repository.GoalRepository
import com.effectiveweek.schedule.domain.repository.RoleRepository
import org.koin.dsl.module

internal val dataModule = module {

    single<GoalRepository> {
        GoalRepositoryImpl(goalDao = get())
    }

    single<RoleRepository> {
        RoleRepositoryImpl(roleDao = get())
    }
}