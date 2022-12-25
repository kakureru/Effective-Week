package com.example.greatweek.data.repository

import com.example.greatweek.data.storage.RoleDao
import com.example.greatweek.data.storage.model.Roles
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.repository.RoleRepository
import kotlinx.coroutines.flow.Flow
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

    override suspend fun getRoleById(roleId: Int): Role {
        val role = roleDao.getRole(roleId = roleId)
        return Role(
            id = role.id,
            name = role.name
        )
    }

    override suspend fun deleteRole(roleId: Int) {
        roleDao.deleteRole(roleId)
    }

    override suspend fun renameRole(roleId: Int, newName: String) {
        roleDao.updateRole(
            Roles(roleId, newName)
        )
    }

    private fun mapToDomain(roles: List<Roles>): List<Role> {
        return roles.map { role ->
            mapToDomain(role)
        }
    }

    private fun mapToDomain(role: Roles): Role {
        return Role(
            id = role.id,
            name = role.name
        )
    }
}