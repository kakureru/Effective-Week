package com.example.schedule.presentation.role_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.domain.repository.RoleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoleDialogViewModel(
    private val roleName: String?,
    private val roleRepository: RoleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoleDialogState(name = roleName ?: ""))
    val uiState: StateFlow<RoleDialogState> = _uiState.asStateFlow()

    fun onNameChanged(text: String) {
        _uiState.update { it.copy(name = text) }
    }

    fun renameRole(oldName: String) = viewModelScope.launch {
        roleRepository.renameRole(oldName = oldName, newName = uiState.value.name)
    }

    fun addRole() = viewModelScope.launch {
        roleRepository.addRole(name = uiState.value.name)
    }

    fun onConfirmClick() {
        if (roleName == null) addRole()
        else renameRole(oldName = roleName)
    }
}