package com.example.greatweek.domain

import com.example.greatweek.domain.model.WeekDay

object Constants {
    val week = listOf(
        WeekDay(1, "Sunday",    listOf()),
        WeekDay(2, "Monday",    listOf()),
        WeekDay(3, "Tuesday ",  listOf()),
        WeekDay(4, "Wednesday", listOf()),
        WeekDay(5, "Thursday",  listOf()),
        WeekDay(6, "Friday",    listOf()),
        WeekDay(7, "Saturday",  listOf())
    )
}