package com.example.greatweek.domain.repository

import com.example.greatweek.domain.model.Role
import kotlinx.coroutines.flow.Flow

interface RoleRepository {

    fun addRole(name: String)

    fun getRoles(): Flow<List<Role>>
}