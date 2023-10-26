package com.effectiveweek.app

import android.app.Application
import com.effectiveweek.app.di.dataModule
import com.effectiveweek.schedule.di.scheduleModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(dataModule, scheduleModule)
        }
    }
}