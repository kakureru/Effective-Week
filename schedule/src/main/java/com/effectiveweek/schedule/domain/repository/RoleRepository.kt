package com.effectiveweek.schedule.domain.repository

import com.effectiveweek.schedule.domain.model.Role
import kotlinx.coroutines.flow.Flow

interface RoleRepository {

    fun getRoles(): Flow<List<Role>>

    suspend fun addRole(name: String)

    suspend fun deleteRole(name: String)

    suspend fun renameRole(oldName: String, newName: String)
}