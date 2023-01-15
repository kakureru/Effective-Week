package com.example.greatweek.app.di

import com.example.greatweek.domain.repository.GoalRepository
import com.example.greatweek.domain.repository.RoleRepository
import com.example.greatweek.domain.usecase.goal.*
import com.example.greatweek.domain.usecase.role.*
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideAddRoleUseCase(roleRepository: RoleRepository): AddRoleUseCase {
        return AddRoleUseCase(roleRepository = roleRepository)
    }

    @Provides
    fun provideAddGoalUseCase(goalRepository: GoalRepository): AddGoalUseCase {
        return AddGoalUseCase(goalRepository = goalRepository)
    }

    @Provides
    fun provideCompleteGoalUseCase(goalRepository: GoalRepository): CompleteGoalUseCase {
        return CompleteGoalUseCase(goalRepository = goalRepository)
    }

    @Provides
    fun provideDeleteRoleUseCase(roleRepository: RoleRepository): DeleteRoleUseCase {
        return DeleteRoleUseCase(roleRepository = roleRepository)
    }

    @Provides
    fun provideDropGoalToRoleUseCase(goalRepository: GoalRepository): DropGoalToRoleUseCase {
        return DropGoalToRoleUseCase(goalRepository = goalRepository)
    }

    @Provides
    fun provideDropGoalToWeekUseCase(goalRepository: GoalRepository): DropGoalToWeekUseCase {
        return DropGoalToWeekUseCase(goalRepository = goalRepository)
    }

    @Provides
    fun provideGetGoalsForDatesUseCase(goalRepository: GoalRepository): GetGoalsForDatesUseCase {
        return GetGoalsForDatesUseCase(goalRepository = goalRepository)
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

    @Provides
    fun provideGetRolesUseCase(roleRepository: RoleRepository): GetRolesUseCase {
        return GetRolesUseCase(roleRepository = roleRepository)
    }

    @Provides
    fun provideRenameRoleUseCase(roleRepository: RoleRepository): RenameRoleUseCase {
        return RenameRoleUseCase(roleRepository = roleRepository)
    }

    @Provides
    fun provideEditGoalUseCase(goalRepository: GoalRepository): EditGoalUseCase {
        return EditGoalUseCase(goalRepository = goalRepository)
    }

    @Provides
    fun provideGetGoalUseCase(goalRepository: GoalRepository): GetGoalUseCase {
        return GetGoalUseCase(goalRepository = goalRepository)
    }
}