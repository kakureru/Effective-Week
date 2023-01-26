package com.example.greatweek.data.repository

import com.example.greatweek.data.db.RoleDao
import com.example.greatweek.data.model.Roles
import com.example.greatweek.data.model.toDomain
import com.example.greatweek.domain.repository.RoleRepository
import kotlinx.coroutines.flow.map

class RoleRepositoryImpl(
    private val roleDao: RoleDao
) : RoleRepository {

    override val allRoles = roleDao.getRoles().map { roles ->
        roles.map { it.toDomain() }
    }

    override suspend fun addRole(name: String) {
        val role = Roles(name = name)
        roleDao.addRole(role)
    }

    override suspend fun deleteRole(name: String) {
        roleDao.deleteRole(name)
    }

    override suspend fun renameRole(oldName: String, newName: String) {
        roleDao.updateRole(oldName, newName)
    }
}