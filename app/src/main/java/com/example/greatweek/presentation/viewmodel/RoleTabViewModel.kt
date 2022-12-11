package com.example.greatweek.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesUseCase
import kotlinx.coroutines.flow.Flow

class RoleTabViewModel(
    private val getRolesUseCase: GetRolesUseCase,
    private val addRoleUseCase: AddRoleUseCase
): ViewModel() {

    fun getRoles(): Flow<List<Role>> {
        return getRolesUseCase.execute()
    }

    fun addRole(name: String) {
        addRoleUseCase.execute(name = name)
    }
}