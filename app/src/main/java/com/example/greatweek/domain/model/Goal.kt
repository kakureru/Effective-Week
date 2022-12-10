package com.example.greatweek.domain.model

data class Goal(
    val title: String,
    val description: String = "",
    val role: String = "",
    val weekday: Int = 0,
    val type: String = ""
)