package com.example.schedule.di.modules

import com.example.schedule.domain.repository.GoalRepository
import com.example.schedule.domain.repository.RoleRepository
import com.example.schedule.domain.usecase.GetScheduleForDatesUseCase
import com.example.schedule.domain.usecase.goal.DropGoalToRoleUseCase
import com.example.schedule.domain.usecase.goal.DropGoalToWeekUseCase
import com.example.schedule.domain.usecase.role.GetRolesWithGoalsUseCase
import dagger.Module
import dagger.Provides

@Module
internal object DomainModule {

    @Provides
    fun provideDropGoalToRoleUseCase(goalRepository: GoalRepository): DropGoalToRoleUseCase =
        DropGoalToRoleUseCase(goalRepository = goalRepository)

    @Provides
    fun provideDropGoalToWeekUseCase(goalRepository: GoalRepository): DropGoalToWeekUseCase =
        DropGoalToWeekUseCase(goalRepository = goalRepository)

    @Provides
    fun provideGetRolesWithGoalsUseCase(
        roleRepository: RoleRepository,
        goalRepository: GoalRepository
    ): GetRolesWithGoalsUseCase =
        GetRolesWithGoalsUseCase(
            roleRepository = roleRepository,
            goalRepository = goalRepository
        )

    @Provides
    fun provideGetScheduleForDatesUseCase(goalRepository: GoalRepository): GetScheduleForDatesUseCase =
        GetScheduleForDatesUseCase(goalRepository)
}