package com.example.greatweek.data.repository

import androidx.lifecycle.Transformations.map
import com.example.greatweek.data.storage.RoleDao
import com.example.greatweek.data.storage.model.Roles
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.repository.RoleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoleRepositoryImpl(private val roleDao: RoleDao) : RoleRepository {
    override fun addRole(name: String) {
        val role = Roles(name = name)
        roleDao.addRole(role)
    }

    override fun getRoles(): Flow<List<Role>> {
        return roleDao.getRoles().map { inRoles ->
            inRoles.map { role -> Role(role.name) }
        }
    }
}