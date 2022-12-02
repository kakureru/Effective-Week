package com.example.greatweek.data.storage.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Goal(
    @PrimaryKey val id: Int,
    @NonNull val title: String,
    val description: String
)