package com.example.greatweek.data.repository

import com.example.greatweek.data.db.RoleDao
import com.example.greatweek.data.model.Roles
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.repository.RoleRepository
import kotlinx.coroutines.flow.map

class RoleRepositoryImpl(
    private val roleDao: RoleDao
) : RoleRepository {

    override val allRoles = roleDao.getRoles().map {
        mapToDomain(it)
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

    private fun mapToDomain(roles: List<Roles>): List<Role> {
        return roles.map { role ->
            mapToDomain(role)
        }
    }

    private fun mapToDomain(role: Roles): Role {
        return Role(name = role.name)
    }
}