package com.example.greatweek.domain.usecase.role

import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.repository.RoleRepository
import kotlinx.coroutines.flow.Flow

class GetRolesUseCase(private val roleRepository: RoleRepository) {
    fun execute(): Flow<List<Role>> {
        return roleRepository.allRoles
    }
}