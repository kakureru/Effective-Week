package com.effectiveweek.core.data

import androidx.room.TypeConverter
import java.time.*

class Converters {
    @TypeConverter
    fun dateFromTimestamp(value: Long?): LocalDate? {
        return value?.let {
            LocalDate.ofEpochDay(value)
        }
    }

    @TypeConverter
    fun dateToTimestamp(value: LocalDate?): Long? {
        return value?.toEpochDay()
    }

    @TypeConverter
    fun timeFromSeconds(value: Int?): LocalTime? {
        return if (value == null)
            null
        else
            LocalTime.ofSecondOfDay(value.toLong())
    }

    @TypeConverter
    fun timeToSeconds(value: LocalTime?): Int? {
        return value?.toSecondOfDay()
    }

    @TypeConverter
    fun dateTimeToTimestamp(value: LocalDateTime?): Long? {
        return if (value == null)
            null
        else
            ZonedDateTime.of(value, ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    @TypeConverter
    fun dateTimeFromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
        }
    }
}