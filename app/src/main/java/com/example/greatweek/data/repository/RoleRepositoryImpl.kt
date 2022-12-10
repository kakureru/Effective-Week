package com.example.greatweek.data.repository

import com.example.greatweek.data.storage.RoleDao
import com.example.greatweek.data.storage.model.Roles
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.repository.RoleRepository

class RoleRepositoryImpl(private val roleDao: RoleDao) : RoleRepository {
    override fun addRole(name: String) {
        val role = Roles(name = name)
        roleDao.addRole(role)
    }

    override fun getRoles(): MutableList<Role> {
        return mapToRole(roleDao.getRoles())
    }

    private fun mapToRole(inRoles: List<Roles>): MutableList<Role> {
        val outRoles = mutableListOf<Role>()
        for (role in inRoles) {
            outRoles.add(
                Role(
                    role.name
                )
            )
        }
        return outRoles
    }
}