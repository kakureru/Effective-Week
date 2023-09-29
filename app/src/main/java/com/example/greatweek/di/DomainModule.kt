package com.example.greatweek.di

import com.example.greatweek.domain.repository.GoalRepository
import com.example.greatweek.domain.repository.RoleRepository
import com.example.greatweek.domain.usecase.goal.DropGoalToRoleUseCase
import com.example.greatweek.domain.usecase.goal.DropGoalToWeekUseCase
import com.example.greatweek.domain.usecase.role.GetRolesWithGoalsUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideDropGoalToRoleUseCase(goalRepository: GoalRepository): DropGoalToRoleUseCase {
        return DropGoalToRoleUseCase(goalRepository = goalRepository)
    }

    @Provides
    fun provideDropGoalToWeekUseCase(goalRepository: GoalRepository): DropGoalToWeekUseCase {
        return DropGoalToWeekUseCase(goalRepository = goalRepository)
    }

    @Provides
    fun provideGetRolesWithGoalsUseCase(
        roleRepository: RoleRepository,
        goalRepository: GoalRepository
    ): GetRolesWithGoalsUseCase {
        return GetRolesWithGoalsUseCase(
            roleRepository = roleRepository,
            goalRepository = goalRepository
        )
    }
}