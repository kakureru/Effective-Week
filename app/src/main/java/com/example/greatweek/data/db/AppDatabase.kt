package com.example.greatweek.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.greatweek.data.db.model.DataVersion
import com.example.greatweek.data.db.model.Goals
import com.example.greatweek.data.db.model.Roles

@Database(entities = [Goals::class, Roles::class, DataVersion::class], version = 8)
abstract class AppDatabase : RoomDatabase() {
    abstract fun GoalDao(): GoalDao
    abstract fun RoleDao(): RoleDao
    abstract fun DataVersionDao(): DataVersionDao
}