package com.example.greatweek.domain.repository

import com.example.greatweek.domain.model.Role

interface RoleRepository {

    fun addRole(name: String)

    fun getRoles(): MutableList<Role>
}