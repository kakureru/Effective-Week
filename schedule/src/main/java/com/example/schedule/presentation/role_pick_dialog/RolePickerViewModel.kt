package com.example.schedule.presentation.role_pick_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.domain.model.Role
import com.example.schedule.domain.repository.RoleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RolePickerViewModel(
    roleRepository: RoleRepository,
) : ViewModel() {

    val roles: StateFlow<List<RoleItem>> = roleRepository.getRoles().map<List<Role>, List<RoleItem>> { list -> list.map { it.toRoleItem() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000L), emptyList())
}