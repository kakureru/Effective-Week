package com.example.greatweek.app.di

import android.app.Application
import androidx.room.Room
import com.example.greatweek.data.repository.GoalRepositoryImpl
import com.example.greatweek.data.repository.RoleRepositoryImpl
import com.example.greatweek.data.storage.AppDatabase
import com.example.greatweek.data.storage.GoalDao
import com.example.greatweek.data.storage.RoleDao
import com.example.greatweek.domain.repository.GoalRepository
import com.example.greatweek.domain.repository.RoleRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule(val application: Application) {

    @Singleton
    @Provides
    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration() //!!!
            .build()
    }

    @Singleton
    @Provides
    fun provideGoalDao(database: AppDatabase): GoalDao {
        return database.GoalDao()
    }

    @Singleton
    @Provides
    fun provideRoleDao(database: AppDatabase): RoleDao {
        return database.RoleDao()
    }

    @Singleton
    @Provides
    fun provideGoalRepository(goalDao: GoalDao): GoalRepository {
        return GoalRepositoryImpl(goalDao = goalDao)
    }

    @Singleton
    @Provides
    fun provideRoleRepository(roleDao: RoleDao): RoleRepository {
        return RoleRepositoryImpl(roleDao = roleDao)
    }

}