package com.effectiveweek.schedule.domain.model

internal data class Role(
    val name: String,
    var goals: List<Goal>,
)