package com.example.greatweek.data.repository

import com.example.greatweek.data.db.RoleDao
import com.example.greatweek.data.db.model.Roles
import com.example.greatweek.data.db.model.toDomain
import com.example.greatweek.domain.repository.BaseRepository
import com.example.greatweek.domain.repository.DataVersionRepository
import com.example.greatweek.domain.repository.RoleRepository
import kotlinx.coroutines.flow.map

class RoleRepositoryImpl(
    private val roleDao: RoleDao,
    dataVersionRepository: DataVersionRepository
) : BaseRepository(dataVersionRepository), RoleRepository {

    override val allRoles = roleDao.getRoles().map { roles ->
        roles.map { it.toDomain() }
    }

    override suspend fun addRole(name: String) = doEntry {
        val role = Roles(name = name)
        roleDao.addRole(role)
    }

    override suspend fun deleteRole(name: String) = doEntry {
        roleDao.deleteRole(name)
    }

    override suspend fun renameRole(oldName: String, newName: String) = doEntry {
        roleDao.updateRole(oldName, newName)
    }
}