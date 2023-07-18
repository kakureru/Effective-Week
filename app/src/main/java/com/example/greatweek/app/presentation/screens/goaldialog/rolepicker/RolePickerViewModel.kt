package com.example.greatweek.app.presentation.screens.goaldialog.rolepicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.repository.RoleRepository
import javax.inject.Inject

class RolePickerViewModel @Inject constructor(
    roleRepository: RoleRepository,
) : ViewModel() {

    val roles: LiveData<List<Role>> = roleRepository.getRoles().asLiveData()
}