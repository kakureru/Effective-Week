package com.example.greatweek.domain.usecase.role

import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.repository.RoleRepository

class GetRoleUseCase(private val roleRepository: RoleRepository) {
    suspend fun execute(roleId: Int): Role {
        return roleRepository.getRoleById(roleId = roleId)
    }
}