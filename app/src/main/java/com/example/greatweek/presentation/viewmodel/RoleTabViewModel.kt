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
    private val deleteRoleUseCase: DeleteRoleUseCase
): ViewModel() {

    fun getRoles(): Flow<List<Role>> {
        return getRolesUseCase.execute()
    }

    fun deleteRole(roleId: Int) {
        deleteRoleUseCase.execute(roleId = roleId)
    }
}