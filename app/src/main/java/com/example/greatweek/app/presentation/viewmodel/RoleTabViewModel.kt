package com.example.greatweek.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.usecase.goal.CompleteGoalUseCase
import com.example.greatweek.domain.usecase.role.DeleteRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesUseCase
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalArgumentException

class RoleTabViewModel(
    private val getRolesUseCase: GetRolesUseCase,
    private val deleteRoleUseCase: DeleteRoleUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase
): ViewModel() {

    fun getRoles(): Flow<List<Role>> {
        return getRolesUseCase.execute()
    }

    fun deleteRole(roleId: Int) {
        deleteRoleUseCase.execute(roleId = roleId)
    }

    fun completeGoal(goalId: Int) {
        completeGoalUseCase.execute(goalId = goalId)
    }
}

class RoleTabViewModelFactory(
    private val getRolesUseCase: GetRolesUseCase,
    private val deleteRoleUseCase: DeleteRoleUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoleTabViewModel::class.java)) {
            return RoleTabViewModel(
                getRolesUseCase,
                deleteRoleUseCase,
                completeGoalUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}