package com.example.greatweek.domain.model.network

data class UserSignUp(
    val id: Int? = null,
    val username: String,
    val password: String,
    val email: String? = null
)