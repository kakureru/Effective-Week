package com.example.greatweek.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.greatweek.data.db.model.GoalEntity
import com.example.greatweek.data.db.model.RoleEntity

@Database(entities = [GoalEntity::class, RoleEntity::class], version = 11)
abstract class AppDatabase : RoomDatabase() {
    abstract fun GoalDao(): GoalDao
    abstract fun RoleDao(): RoleDao
}