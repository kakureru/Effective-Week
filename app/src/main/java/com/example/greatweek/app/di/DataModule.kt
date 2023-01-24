package com.example.greatweek.app.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.greatweek.app.presentation.constants.USER_PREFERENCES_NAME
import com.example.greatweek.data.db.AppDatabase
import com.example.greatweek.data.db.GoalDao
import com.example.greatweek.data.db.RoleDao
import com.example.greatweek.data.network.GreatWeekApi
import com.example.greatweek.data.repository.GoalRepositoryImpl
import com.example.greatweek.data.repository.RoleRepositoryImpl
import com.example.greatweek.data.repository.UserRepositoryImpl
import com.example.greatweek.domain.repository.GoalRepository
import com.example.greatweek.domain.repository.RoleRepository
import com.example.greatweek.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
class DataModule(val application: Application) {

    private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)

    @Singleton
    @Provides
    fun providePreferencesDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES_NAME) }
        )
    }

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

    @Singleton
    @Provides
    fun provideUserRepository(
        greatWeekApi: GreatWeekApi,
        preferencesDataStore: DataStore<Preferences>
    ): UserRepository {
        return UserRepositoryImpl(
            greatWeekApi = greatWeekApi,
            prefDataStore = preferencesDataStore
        )
    }

}