package com.example.greatweek.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class Goal(
    val id: Int = -1,
    val title: String,
    val description: String = "",
    val role: String = "",
    val date: LocalDate? = null,
    val time: LocalTime? = null,
    val commitment: Boolean = false
)