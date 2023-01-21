package com.example.greatweek.data.model

import com.google.gson.annotations.SerializedName

data class RemoteUser(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String? = null
)