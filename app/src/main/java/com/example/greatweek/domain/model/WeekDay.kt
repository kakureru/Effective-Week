package com.example.greatweek.domain.model

import java.time.LocalDate

data class WeekDay(
    val date: LocalDate,
    var goals: List<Goal>
)