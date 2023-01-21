package com.example.greatweek.app.di

import android.content.SharedPreferences
import com.example.greatweek.app.presentation.constants.AUTH_TOKEN_KEY
import com.example.greatweek.app.presentation.constants.NEED_TOKEN_HEADER
import com.example.greatweek.data.network.GreatWeekApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        sharedPreferences: SharedPreferences
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val needToken = request.headers[NEED_TOKEN_HEADER] != "false"
                val requestBuilder = request
                    .newBuilder()
                    .removeHeader(NEED_TOKEN_HEADER)
                if (needToken)
                    requestBuilder.addHeader(
                        "Authorization",
                        "Token ${sharedPreferences.getString(AUTH_TOKEN_KEY, "").orEmpty()}"
                    )
                return@addInterceptor chain.proceed(requestBuilder.build())
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideGreatWeekApi(retrofit: Retrofit): GreatWeekApi {
        return retrofit.create(GreatWeekApi::class.java)
    }
}