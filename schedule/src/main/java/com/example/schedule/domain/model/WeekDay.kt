package com.example.schedule.domain.model

import java.time.LocalDate

data class WeekDay(
    val date: LocalDate,
    var goals: List<Goal>
)