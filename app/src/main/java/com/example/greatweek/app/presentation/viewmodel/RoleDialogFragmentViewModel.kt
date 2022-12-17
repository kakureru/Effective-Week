package com.example.greatweek.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.domain.usecase.role.RenameRoleUseCase
import java.lang.IllegalArgumentException

class RoleDialogFragmentViewModel(
    private val addRoleUseCase: AddRoleUseCase,
    private val renameRoleUseCase: RenameRoleUseCase
): ViewModel() {

    fun renameRole(roleId: Int, newName: String) {
        renameRoleUseCase.execute(roleId = roleId, newName = newName)
    }

    fun addRole(name: String) {
        addRoleUseCase.execute(name = name)
    }
}

class RoleDialogFragmentViewModelFactory(
    private val addRoleUseCase: AddRoleUseCase,
    private val renameRoleUseCase: RenameRoleUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoleDialogFragmentViewModel::class.java)) {
            return RoleDialogFragmentViewModel(
                addRoleUseCase,
                renameRoleUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}