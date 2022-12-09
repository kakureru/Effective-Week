package com.example.greatweek.data.repository

import com.example.greatweek.data.storage.RoleDao
import com.example.greatweek.data.storage.model.Roles
import com.example.greatweek.domain.repository.RoleRepository

class RoleRepositoryImpl(private val roleDao: RoleDao) : RoleRepository {
    override fun addRole(name: String) {
        val role = Roles(name = name)
        roleDao.addRole(role)
    }
}