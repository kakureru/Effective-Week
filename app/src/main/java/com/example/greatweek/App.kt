package com.example.greatweek

import android.app.Application
import com.example.greatweek.di.AppComponent
import com.example.greatweek.di.AppModule
import com.example.greatweek.di.DaggerAppComponent
import com.example.greatweek.di.DataModule
import com.example.schedule.di.deps.ScheduleDepsStore

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(context = this))
            .dataModule(DataModule(application = this@App))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        ScheduleDepsStore.deps = appComponent
    }
}