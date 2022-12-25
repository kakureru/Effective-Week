package com.example.greatweek.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.domain.usecase.role.RenameRoleUseCase
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RoleDialogFragmentViewModel(
    private val addRoleUseCase: AddRoleUseCase,
    private val renameRoleUseCase: RenameRoleUseCase
): ViewModel() {

    fun renameRole(roleId: Int, newName: String) = viewModelScope.launch {
        renameRoleUseCase.execute(roleId = roleId, newName = newName)
    }

    fun addRole(name: String) = viewModelScope.launch {
        addRoleUseCase.execute(name = name)
    }
}