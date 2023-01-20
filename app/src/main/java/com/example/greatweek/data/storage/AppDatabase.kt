package com.example.greatweek.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.greatweek.data.model.Goals
import com.example.greatweek.data.model.Roles

@Database(entities = [Goals::class, Roles::class], version = 7)
abstract class AppDatabase : RoomDatabase() {
    abstract fun GoalDao(): GoalDao
    abstract fun RoleDao(): RoleDao
}