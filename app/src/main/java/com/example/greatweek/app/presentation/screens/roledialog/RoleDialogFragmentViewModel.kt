package com.example.greatweek.app.presentation.screens.roledialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

@Suppress("UNCHECKED_CAST")
class RoleDialogFragmentViewModelFactory(
    private val addRoleUseCase: AddRoleUseCase,
    private val renameRoleUseCase: RenameRoleUseCase
) : ViewModelProvider.Factory {
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