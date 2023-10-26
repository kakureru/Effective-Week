package com.effectiveweek.schedule.di

import com.effectiveweek.schedule.domain.usecase.GetScheduleForDatesUseCase
import com.effectiveweek.schedule.domain.usecase.goal.CompleteGoalUseCase
import com.effectiveweek.schedule.domain.usecase.goal.DropGoalToRoleUseCase
import com.effectiveweek.schedule.domain.usecase.goal.DropGoalToWeekUseCase
import com.effectiveweek.schedule.domain.usecase.role.GetRolesWithGoalsUseCase
import org.koin.dsl.module

internal val domainModule = module {

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

    factory {
        CompleteGoalUseCase(goalRepository = get())
    }
}