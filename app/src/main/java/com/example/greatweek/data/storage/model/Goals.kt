package com.example.greatweek.data.storage.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.greatweek.data.storage.Converters
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