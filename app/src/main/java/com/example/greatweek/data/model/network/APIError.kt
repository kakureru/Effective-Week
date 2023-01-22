package com.example.greatweek.data.model.network

data class APIError(
    val non_field_errors: List<String> = listOf("Unknown error")
)