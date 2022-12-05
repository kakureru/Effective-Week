package com.example.greatweek.domain.model

data class WeekDay(
    val id: Int,
    val name: String,
    val goals: MutableList<Goal>
)