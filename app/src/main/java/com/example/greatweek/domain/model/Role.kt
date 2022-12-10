package com.example.greatweek.domain.model

data class Role(
    val name: String,
    val goals: MutableList<Goal> = mutableListOf()
)