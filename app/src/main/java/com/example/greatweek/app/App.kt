package com.example.greatweek.app

import android.app.Application
import com.example.greatweek.app.di.AppComponent
import com.example.greatweek.app.di.AppModule
import com.example.greatweek.app.di.DaggerAppComponent
import com.example.greatweek.app.di.DataModule

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(context = this))
            .dataModule(DataModule(application = this@App))
            .build()
    }
}