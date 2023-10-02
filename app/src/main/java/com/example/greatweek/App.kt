package com.example.greatweek

import android.app.Application
import com.example.greatweek.di.dataModule
import com.example.greatweek.di.domainModule
import com.example.greatweek.di.navigationModule
import com.example.greatweek.di.viewModelModule
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
            modules(dataModule, navigationModule, viewModelModule, domainModule)
        }
    }
}