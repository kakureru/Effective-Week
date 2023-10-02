package com.example.schedule.data.repository

import com.example.schedule.data.db.RoleDao
import com.example.schedule.data.db.model.RoleEntity
import com.example.schedule.domain.model.Role
import com.example.schedule.domain.repository.RoleRepository
import kotlinx.coroutines.flow.map

class RoleRepositoryImpl(
    private val roleDao: RoleDao,
) : RoleRepository {

    override fun getRoles() = roleDao.getRoles().map { roles ->
        roles.map { it.toDomain() }
    }

    override suspend fun addRole(name: String) {
        val role = RoleEntity(name = name)
        roleDao.addRole(role)
    }

    override suspend fun deleteRole(name: String) {
        roleDao.deleteRole(name)
    }

    override suspend fun renameRole(oldName: String, newName: String) {
        roleDao.updateRole(oldName, newName)
    }

    private fun RoleEntity.toDomain() = Role(
        name = name
    )
}