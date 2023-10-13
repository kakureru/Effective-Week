package com.effectiveweek.schedule.presentation.role_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effectiveweek.schedule.R
import com.effectiveweek.schedule.domain.repository.RoleRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoleDialogViewModel(
    private val roleName: String?,
    private val roleRepository: RoleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoleDialogState(name = roleName ?: ""))
    val uiState: StateFlow<RoleDialogState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<RoleDialogEffect>()
    val uiEffect: SharedFlow<RoleDialogEffect> = _uiEffect.asSharedFlow()

    fun onNameChanged(text: String) {
        _uiState.update { it.copy(name = text) }
    }

    private fun renameRole(oldName: String) = viewModelScope.launch {
        roleRepository.renameRole(oldName = oldName, newName = uiState.value.name)
    }

    private fun addRole() = viewModelScope.launch {
        roleRepository.addRole(name = uiState.value.name)
    }

    fun onConfirmClick() = viewModelScope.launch {
        if (isInputCorrect.not()) {
            _uiEffect.emit(RoleDialogEffect.Error(R.string.error_name_required))
        } else {
            if (roleName == null) addRole()
            else renameRole(oldName = roleName)
            _uiState.update { it.copy(navState = RoleDialogNavState.Dismiss) }
        }
    }

    private val isInputCorrect get() = uiState.value.name.isNotEmpty()
}