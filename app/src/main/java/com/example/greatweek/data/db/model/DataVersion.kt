package com.example.greatweek.data.db.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.greatweek.data.db.Converters
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "data_version")
@TypeConverters(Converters::class)
data class DataVersion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @NonNull val date: LocalDateTime
)