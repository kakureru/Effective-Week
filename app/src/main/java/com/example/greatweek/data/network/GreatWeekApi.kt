package com.example.greatweek.data.network

import com.example.greatweek.app.presentation.constants.NEED_TOKEN_HEADER
import com.example.greatweek.data.model.AuthToken
import com.example.greatweek.data.model.RemoteUser
import com.example.greatweek.data.model.RemoteUserLogin
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GreatWeekApi {

    @Headers("$NEED_TOKEN_HEADER: false")
    @POST("api/v1/auth/users/")
    suspend fun register(@Body remoteUser: RemoteUser): RemoteUser

    @Headers("$NEED_TOKEN_HEADER: false")
    @POST("auth/token/login/")
    suspend fun login(@Body remoteUserLogin: RemoteUserLogin): AuthToken
}