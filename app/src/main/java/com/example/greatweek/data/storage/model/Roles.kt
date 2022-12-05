package com.example.greatweek.data.storage.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Roles(
    @PrimaryKey val id: Int,
    @NonNull val name: String
)