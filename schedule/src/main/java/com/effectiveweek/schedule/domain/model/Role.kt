package com.effectiveweek.schedule.domain.model

data class Role(
    val name: String,
    var goals: List<Goal>,
)