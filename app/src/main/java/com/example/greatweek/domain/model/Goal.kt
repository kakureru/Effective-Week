package com.example.greatweek.domain.model

data class Goal(
    val id: Int = -1,
    val title: String,
    val description: String = "",
    val role: String = "",
    val weekday: Int = 0,
    val commitment: Boolean = false
)