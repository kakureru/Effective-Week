package com.example.greatweek.domain.usecase.role

import com.example.greatweek.domain.repository.RoleRepository

class RenameRoleUseCase(private val roleRepository: RoleRepository) {
    suspend fun execute(oldName: String, newName: String) {
        roleRepository.renameRole(oldName = oldName, newName = newName)
    }
}