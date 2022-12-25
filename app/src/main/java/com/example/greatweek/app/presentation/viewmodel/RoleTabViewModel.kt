package com.example.greatweek.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.usecase.goal.CompleteGoalUseCase
import com.example.greatweek.domain.usecase.role.DeleteRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RoleTabViewModel(
    private val getRolesUseCase: GetRolesUseCase,
    private val deleteRoleUseCase: DeleteRoleUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase
): ViewModel() {

    fun getRoles(): Flow<List<Role>> {
        return getRolesUseCase.execute()
    }

    fun deleteRole(roleId: Int) = viewModelScope.launch {
        deleteRoleUseCase.execute(roleId = roleId)
    }

    fun completeGoal(goalId: Int) = viewModelScope.launch {
        completeGoalUseCase.execute(goalId = goalId)
    }
}