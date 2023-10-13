package com.example.schedule.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class Goal(
    val id: Int,
    val title: String,
    val description: String,
    val role: String?,
    val date: LocalDate?,
    val time: LocalTime?,
    val appointment: Boolean,
)