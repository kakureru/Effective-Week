package com.example.greatweek.domain.model

data class Role(
    val id: Int = 0,
    val name: String,
    val goals: MutableList<Goal> = mutableListOf()
)