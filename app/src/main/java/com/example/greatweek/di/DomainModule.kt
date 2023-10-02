package com.example.greatweek.di

import com.example.schedule.domain.usecase.GetScheduleForDatesUseCase
import com.example.schedule.domain.usecase.goal.DropGoalToRoleUseCase
import com.example.schedule.domain.usecase.goal.DropGoalToWeekUseCase
import com.example.schedule.domain.usecase.role.GetRolesWithGoalsUseCase
import org.koin.dsl.module

val domainModule = module {

    factory {
        DropGoalToRoleUseCase(goalRepository = get())
    }

    factory {
        DropGoalToWeekUseCase(goalRepository = get())
    }

    factory {
        GetRolesWithGoalsUseCase(
            roleRepository = get(),
            goalRepository = get()
        )
    }

    factory {
        GetScheduleForDatesUseCase(goalRepository = get())
    }
}