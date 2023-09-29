package com.example.greatweek

import android.app.Application
import com.example.greatweek.di.AppComponent
import com.example.greatweek.di.AppModule
import com.example.greatweek.di.DaggerAppComponent
import com.example.greatweek.di.DataModule

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