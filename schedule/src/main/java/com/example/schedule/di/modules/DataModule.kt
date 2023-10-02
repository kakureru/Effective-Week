package com.example.schedule.di.modules

import com.example.schedule.data.db.GoalDao
import com.example.schedule.data.db.RoleDao
import com.example.schedule.data.repository.GoalRepositoryImpl
import com.example.schedule.data.repository.RoleRepositoryImpl
import com.example.schedule.di.ScheduleScope
import com.example.schedule.domain.repository.GoalRepository
import com.example.schedule.domain.repository.RoleRepository
import dagger.Module
import dagger.Provides

@Module
internal object DataModule {

    @ScheduleScope
    @Provides
    fun provideGoalRepository(
        goalDao: GoalDao,
    ): GoalRepository {
        return GoalRepositoryImpl(
            goalDao = goalDao,
        )
    }

    @ScheduleScope
    @Provides
    fun provideRoleRepository(
        roleDao: RoleDao,
    ): RoleRepository = RoleRepositoryImpl(
        roleDao = roleDao,
    )
}