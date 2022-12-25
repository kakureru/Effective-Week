package com.example.greatweek.domain.repository

import com.example.greatweek.domain.model.Role
import kotlinx.coroutines.flow.Flow

interface RoleRepository {

    val allRoles: Flow<List<Role>>

    suspend fun addRole(name: String)

    suspend fun deleteRole(roleId: Int)

    suspend fun renameRole(roleId: Int, newName: String)

    suspend fun getRoleById(roleId: Int): Role
}