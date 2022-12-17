package com.example.greatweek.domain.model

data class Goal(
    val id: Int = -1,
    val title: String,
    val description: String = "",
    val roleId: Int = -1,
    val weekday: Int = 0,
    val commitment: Boolean = false
)