package com.example.greatweek.data.model.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.greatweek.data.db.Converters
import com.example.greatweek.domain.model.Goal
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Roles::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("role"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )]
)
@TypeConverters(Converters::class)
data class Goals(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @NonNull val title: String,
    val description: String,
    @NonNull val role: String,
    val date: LocalDate?,
    val time: LocalTime?,
    val commitment: Boolean
)

fun Goals.toDomain() = Goal(
    id = id,
    title = title,
    description = description,
    role = role,
    date = date,
    time = time,
    commitment = commitment
)