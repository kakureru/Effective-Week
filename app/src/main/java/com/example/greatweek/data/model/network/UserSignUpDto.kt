package com.example.greatweek.data.model.network

import com.example.greatweek.domain.model.network.UserSignUp
import com.google.gson.annotations.SerializedName

data class UserSignUpDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String? = null
)

fun UserSignUp.fromDomain() = UserSignUpDto(
    id = id,
    username = username,
    password = password,
    email = email
)

fun UserSignUpDto.toDomain() = UserSignUp(
    id = id,
    username = username,
    password = password,
    email = email
)