package com.example.greatweek.domain.model

import kotlinx.coroutines.flow.Flow

data class WeekDay(
    val id: Int,
    val name: String,
    val goals: Flow<List<Goal>>
)