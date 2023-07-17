package com.example.greatweek.domain.repository

import com.example.greatweek.domain.model.Role
import kotlinx.coroutines.flow.Flow

interface RoleRepository {

    fun getRoles(): Flow<List<Role>>

    suspend fun addRole(name: String)

    suspend fun deleteRole(name: String)

    suspend fun renameRole(oldName: String, newName: String)
}