package com.example.greatweek.app.di

import com.example.greatweek.data.network.GreatWeekApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    /**
     * IP 127.0.0.1 is the local IP of the emulator,
     * IP 10.0.2.2 points to 127.0.0.1 of the computer
     */
    private val BASE_URL = "http://10.0.2.2:8000/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideGreatWeekApi(retrofit: Retrofit): GreatWeekApiService {
        return retrofit.create(GreatWeekApiService::class.java)
    }
}