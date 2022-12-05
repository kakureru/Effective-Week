package com.example.greatweek.presentation

import android.app.Application
import com.example.greatweek.data.storage.AppDatabase

class GreatWeekApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}