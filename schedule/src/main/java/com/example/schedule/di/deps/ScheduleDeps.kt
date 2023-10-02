package com.example.schedule.di.deps

import com.example.schedule.data.db.GoalDao
import com.example.schedule.data.db.RoleDao

interface ScheduleDeps {
    fun goalDao(): GoalDao
    fun roleDao(): RoleDao
}