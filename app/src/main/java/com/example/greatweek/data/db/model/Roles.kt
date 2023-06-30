package com.example.greatweek.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.greatweek.domain.model.Role

@Entity
data class Roles(
    @PrimaryKey val name: String
)

fun Roles.toDomain() = Role(
    name = name
)