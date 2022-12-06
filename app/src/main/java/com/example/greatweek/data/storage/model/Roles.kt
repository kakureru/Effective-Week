package com.example.greatweek.data.storage.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Roles(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @NonNull val name: String
)