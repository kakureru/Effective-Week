package com.example.greatweek.data.network

import com.example.greatweek.data.model.RetrofitCreateUser
import com.example.greatweek.data.model.RetrofitLoginUser
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GreatWeekApi {

    @Headers("needToken: false")
    @POST("api/v1/auth/users/")
    suspend fun register(@Body retrofitCreateUserModel: RetrofitCreateUser): String

    @POST("auth/token/login/")
    suspend fun login(@Body retrofitLoginUserModel: RetrofitLoginUser): String
}