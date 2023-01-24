package com.example.greatweek.data.network

import com.example.greatweek.data.constants.NEED_TOKEN_HEADER
import com.example.greatweek.data.model.network.SignInResponse
import com.example.greatweek.data.model.network.SignUpResponse
import com.example.greatweek.data.model.network.UserSignUpDto
import com.example.greatweek.data.model.network.UserSignInDto
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GreatWeekApi {

    @Headers("$NEED_TOKEN_HEADER: false")
    @POST("api/v1/auth/users/")
    suspend fun signUp(@Body userSignUpDto: UserSignUpDto): SignUpResponse

    @Headers("$NEED_TOKEN_HEADER: false")
    @POST("auth/token/login/")
    suspend fun signIn(@Body userSignInDto: UserSignInDto): SignInResponse
}