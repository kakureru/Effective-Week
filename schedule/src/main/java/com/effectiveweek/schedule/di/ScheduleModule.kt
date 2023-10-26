package com.effectiveweek.schedule.di

import org.koin.dsl.module

val scheduleModule = module {
    includes(domainModule, dataModule, viewModelModule)
}