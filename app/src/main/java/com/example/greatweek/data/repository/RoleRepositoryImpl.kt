package com.example.greatweek.data.repository

import com.example.greatweek.data.SyncManager
import com.example.greatweek.data.db.RoleDao
import com.example.greatweek.data.model.db.Roles
import com.example.greatweek.data.model.db.toDomain
import com.example.greatweek.domain.repository.RoleRepository
import kotlinx.coroutines.flow.map

class RoleRepositoryImpl(
    private val roleDao: RoleDao,
    syncManager: SyncManager
) : BaseRepository(syncManager), RoleRepository {

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