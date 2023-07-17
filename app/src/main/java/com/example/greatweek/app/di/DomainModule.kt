package com.example.greatweek.app.di

import com.example.greatweek.domain.SyncManager
import com.example.greatweek.domain.repository.GoalRepository
import com.example.greatweek.domain.repository.RoleRepository
import com.example.greatweek.domain.repository.UserRepository
import com.example.greatweek.domain.usecase.SyncUseCase
import com.example.greatweek.domain.usecase.authentication.SignInUseCase
import com.example.greatweek.domain.usecase.authentication.SignUpUseCase
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

    @Provides
    fun provideSignInUseCase(userRepository: UserRepository): SignInUseCase {
        return SignInUseCase(userRepository = userRepository)
    }

    @Provides
    fun provideSignUpUseCase(userRepository: UserRepository): SignUpUseCase {
        return SignUpUseCase(userRepository = userRepository)
    }

    @Provides
    fun provideSyncUseCase(syncManager: SyncManager): SyncUseCase = SyncUseCase(
        syncManager = syncManager
    )
}