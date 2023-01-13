package com.example.greatweek.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.domain.usecase.role.RenameRoleUseCase
import kotlinx.coroutines.launch

class RoleDialogFragmentViewModel(
    private val addRoleUseCase: AddRoleUseCase,
    private val renameRoleUseCase: RenameRoleUseCase
): ViewModel() {

    fun renameRole(oldName: String, newName: String) = viewModelScope.launch {
        renameRoleUseCase.execute(oldName = oldName, newName = newName)
    }

    fun addRole(name: String) = viewModelScope.launch {
        addRoleUseCase.execute(name = name)
    }
}