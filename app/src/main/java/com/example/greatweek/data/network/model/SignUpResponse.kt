package com.example.greatweek.data.network.model

import com.example.greatweek.domain.model.network.SignUp
import com.google.gson.annotations.SerializedName

data class SignUpResponse (
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("id") val id: Int
)

fun SignUpResponse.toDomain() = SignUp(
    email = email,
    username = username,
    id = id
)