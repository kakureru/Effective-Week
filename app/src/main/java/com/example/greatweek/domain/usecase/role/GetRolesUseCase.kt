package com.example.greatweek.domain.usecase.role

import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.repository.RoleRepository

class GetRolesUseCase(
    private val roleRepository: RoleRepository
) {
    fun execute(): List<Role> {
        return roleRepository.getRoles()
    }
}