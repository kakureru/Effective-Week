package com.example.greatweek.domain.usecase.role

import com.example.greatweek.domain.repository.RoleRepository

class AddRoleUseCase(private val roleRepository: RoleRepository) {
    fun execute(name: String) {
        roleRepository.addRole(name = name)
    }
}