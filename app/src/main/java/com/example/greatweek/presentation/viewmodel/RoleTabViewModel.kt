package com.example.greatweek.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.domain.usecase.role.DeleteRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesUseCase
import com.example.greatweek.domain.usecase.role.RenameRoleUseCase
import kotlinx.coroutines.flow.Flow

class RoleTabViewModel(
    private val getRolesUseCase: GetRolesUseCase,
    private val addRoleUseCase: AddRoleUseCase,
    private val deleteRoleUseCase: DeleteRoleUseCase,
    private val renameRoleUseCase: RenameRoleUseCase
): ViewModel() {

    fun getRoles(): Flow<List<Role>> {
        return getRolesUseCase.execute()
    }

    fun addRole(name: String) {
        addRoleUseCase.execute(name = name)
    }

    fun deleteRole(roleId: Int) {
        deleteRoleUseCase.execute(roleId = roleId)
    }

    fun renameRole(roleId: Int, newName: String) {
        renameRoleUseCase.execute(roleId = roleId, newName = newName)
    }
}