package com.example.greatweek.domain

import com.example.greatweek.domain.model.WeekDay

object Constants {
    val week = listOf(
        WeekDay(1, "Monday",    listOf()),
        WeekDay(2, "Tuesday ",  listOf()),
        WeekDay(3, "Wednesday", listOf()),
        WeekDay(4, "Thursday",  listOf()),
        WeekDay(5, "Friday",    listOf()),
        WeekDay(6, "Saturday",  listOf()),
        WeekDay(7, "Sunday",    listOf()),
    )
}