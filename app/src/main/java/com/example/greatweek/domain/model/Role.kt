package com.example.greatweek.domain.model

data class Role(
    val name: String,
    var goals: List<Goal> = listOf()
)