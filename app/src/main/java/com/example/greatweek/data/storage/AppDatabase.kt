package com.example.greatweek.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.greatweek.data.storage.model.Goal
import com.example.greatweek.data.storage.model.Role

@Database(entities = [Goal::class, Role::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun GoalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}