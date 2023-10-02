package com.example.greatweek.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import org.koin.dsl.module

val navigationModule = module {

    single {
        Cicerone.create()
    }

    single {
        provideRouter(cicerone = get())
    }

    single {
        provideNavigatorHolder(cicerone = get())
    }
}

fun provideRouter(cicerone: Cicerone<Router>) = cicerone.router
fun provideNavigatorHolder(cicerone: Cicerone<Router>) = cicerone.getNavigatorHolder()