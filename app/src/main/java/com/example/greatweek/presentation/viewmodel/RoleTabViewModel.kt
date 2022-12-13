package com.example.greatweek.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.domain.usecase.role.DeleteRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesUseCase
import com.example.greatweek.domain.usecase.role.RenameRoleUseCase
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalArgumentException

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

class RoleTabViewModelFactory(
    private val getRolesUseCase: GetRolesUseCase,
    private val deleteRoleUseCase: DeleteRoleUseCase,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoleTabViewModel::class.java)) {
            return RoleTabViewModel(
                getRolesUseCase,
                deleteRoleUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}