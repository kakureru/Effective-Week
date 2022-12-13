package com.example.greatweek.domain.model

data class Role(
    val id: Int = 0,
    val name: String,
    var goals: List<Goal> = listOf()
)