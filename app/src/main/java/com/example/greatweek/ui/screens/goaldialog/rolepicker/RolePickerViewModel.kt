package com.example.greatweek.ui.screens.goaldialog.rolepicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.repository.RoleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class RolePickerViewModel @Inject constructor(
    roleRepository: RoleRepository,
) : ViewModel() {

    val roles: StateFlow<List<RoleItem>> = roleRepository.getRoles().map { list -> list.map { it.toRoleItem() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000L), emptyList())
}