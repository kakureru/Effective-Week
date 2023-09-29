package com.example.greatweek.ui.screens.roledialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greatweek.domain.repository.RoleRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoleDialogFragmentViewModel @Inject constructor(
    private val roleRepository: RoleRepository,
): ViewModel() {

    fun renameRole(oldName: String, newName: String) = viewModelScope.launch {
        roleRepository.renameRole(oldName = oldName, newName = newName)
    }

    fun addRole(name: String) = viewModelScope.launch {
        roleRepository.addRole(name = name)
    }
}