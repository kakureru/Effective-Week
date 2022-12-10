package com.example.greatweek.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesUseCase
import java.lang.IllegalArgumentException

class RoleTabViewModelFactory(
    private val getRolesUseCase: GetRolesUseCase,
    private val addRoleUseCase: AddRoleUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoleTabViewModel::class.java)) {
            return RoleTabViewModel(
                getRolesUseCase,
                addRoleUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}