package com.effectiveweek.schedule.domain.model

import java.time.LocalDate

data class ScheduleDay(
    val date: LocalDate,
    var goals: List<Goal>
)