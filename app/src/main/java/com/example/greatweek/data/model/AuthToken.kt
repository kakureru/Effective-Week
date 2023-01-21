package com.example.greatweek.data.model

import com.google.gson.annotations.SerializedName

data class AuthToken(
    @SerializedName("auth_token") val token: String
)