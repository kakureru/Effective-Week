package com.example.greatweek.app

import android.app.Application
import com.example.greatweek.app.di.appModule
import com.example.greatweek.app.di.dataModule
import com.example.greatweek.app.di.domainModule
import com.example.greatweek.data.storage.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class GreatWeekApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@GreatWeekApplication)
            modules(
                listOf(
                    appModule,
                    domainModule,
                    dataModule
                )
            )
        }
    }
}