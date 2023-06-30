package com.example.greatweek.data.network.model

import com.example.greatweek.domain.model.network.UserSignIn
import com.google.gson.annotations.SerializedName

data class UserSignInDto (
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
)

fun UserSignIn.fromDomain() = UserSignInDto(
    username = username,
    password = password
)

