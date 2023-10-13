package com.effectiveweek.app.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.effectiveweek.schedule.data.db.GoalDao
import com.effectiveweek.schedule.data.db.RoleDao
import com.effectiveweek.schedule.data.db.model.GoalEntity
import com.effectiveweek.schedule.data.db.model.RoleEntity

@Database(entities = [GoalEntity::class, RoleEntity::class], version = 11)
abstract class AppDatabase : RoomDatabase() {
    abstract fun GoalDao(): GoalDao
    abstract fun RoleDao(): RoleDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "app_database"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let { return it }
            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val db = Room.databaseBuilder(application, AppDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}