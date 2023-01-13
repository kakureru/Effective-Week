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
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration() //!!!
            .build()
    }

    fun provideGoalDao(database: AppDatabase): GoalDao {
        return database.GoalDao()
    }

    fun provideRoleDao(database: AppDatabase): RoleDao {
        return database.RoleDao()
    }

    single<AppDatabase> {
        provideDatabase(androidApplication())
    }

    single<RoleDao> {
        provideRoleDao(database = get())
    }

    single<GoalDao> {
        provideGoalDao(database = get())
    }

    single<GoalRepository> {
        GoalRepositoryImpl(goalDao = get())
    }

    single<RoleRepository> {
        RoleRepositoryImpl(roleDao = get())
    }

}