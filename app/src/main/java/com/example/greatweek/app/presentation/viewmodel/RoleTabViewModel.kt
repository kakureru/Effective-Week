package com.example.greatweek.app.presentation.viewmodel

import androidx.lifecycle.*
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.usecase.goal.CompleteGoalUseCase
import com.example.greatweek.domain.usecase.role.DeleteRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesWithGoalsUseCase
import kotlinx.coroutines.launch

class RoleTabViewModel(
    private val getRolesWithGoalsUseCase: GetRolesWithGoalsUseCase,
    private val deleteRoleUseCase: DeleteRoleUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase
): ViewModel() {

    val allRoles: LiveData<List<Role>> = getRolesWithGoalsUseCase.execute().asLiveData()

    fun deleteRole(roleId: Int) = viewModelScope.launch {
        deleteRoleUseCase.execute(roleId = roleId)
    }

    fun completeGoal(goalId: Int) = viewModelScope.launch {
        completeGoalUseCase.execute(goalId = goalId)
    }
}