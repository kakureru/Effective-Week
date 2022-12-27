package com.example.greatweek.data.storage.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Roles(
    @PrimaryKey val name: String
)