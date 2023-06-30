package com.example.greatweek.data.network.model

import com.example.greatweek.domain.model.network.SignIn
import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("auth_token") val token: String
)

fun SignInResponse.toDomain() = SignIn(
    token = token
)