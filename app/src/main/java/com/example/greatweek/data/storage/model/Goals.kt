package com.example.greatweek.data.storage.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Roles::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("role"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class Goals(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @NonNull val title: String,
    val description: String,
    @NonNull val role: String,
    val day: Int,
    val commitment: Boolean
)