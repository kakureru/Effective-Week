package com.example.greatweek.data.model

import com.google.gson.annotations.SerializedName

data class RemoteUserLogin (
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
)