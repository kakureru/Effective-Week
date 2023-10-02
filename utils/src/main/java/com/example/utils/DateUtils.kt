package com.example.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.stream.Collectors
import java.util.stream.IntStream

fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> =
    IntStream.iterate(0) { i -> i + 1 }
        .limit(ChronoUnit.DAYS.between(startDate, endDate))
        .mapToObj { i -> startDate.plusDays(i.toLong()) }
        .collect(Collectors.toList())

fun getCurrentFirstWeekDay(): LocalDate {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
    while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
        calendar.add(Calendar.DATE, -1);
    return LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId()).toLocalDate()
}

