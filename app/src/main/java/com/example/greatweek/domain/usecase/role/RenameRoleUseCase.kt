package com.example.greatweek.domain.usecase.role

import com.example.greatweek.domain.repository.RoleRepository

class RenameRoleUseCase(private val roleRepository: RoleRepository) {
    fun execute(roleId: Int, newName: String) {
        roleRepository.renameRole(roleId = roleId, newName = newName)
    }
}