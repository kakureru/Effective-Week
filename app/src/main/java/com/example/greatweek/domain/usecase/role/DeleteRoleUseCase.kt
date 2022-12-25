package com.example.greatweek.domain.usecase.role

import com.example.greatweek.domain.repository.RoleRepository

class DeleteRoleUseCase(private var roleRepository: RoleRepository) {
    suspend fun execute(roleId: Int) {
        roleRepository.deleteRole(roleId)
    }
}