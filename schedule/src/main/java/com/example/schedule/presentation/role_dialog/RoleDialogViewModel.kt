package com.example.schedule.presentation.role_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.schedule.domain.repository.RoleRepository
import com.example.schedule.presentation.goal_dialog.GoalDialogViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class RoleDialogViewModel @Inject constructor(
    private val roleName: String?,
    private val roleRepository: RoleRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(RoleDialogState())
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

class RoleDialogViewModelFactory @AssistedInject constructor(
    @Assisted private val roleName: String?,
    private val roleRepository: RoleRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoleDialogViewModel::class.java))
            return RoleDialogViewModel(roleName, roleRepository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted roleName: String?,
        ): RoleDialogViewModelFactory
    }
}