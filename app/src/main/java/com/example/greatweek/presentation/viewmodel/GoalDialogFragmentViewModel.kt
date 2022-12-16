package com.example.greatweek.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.usecase.goal.AddGoalUseCase
import com.example.greatweek.domain.usecase.role.GetRoleUseCase
import java.lang.IllegalArgumentException

class GoalDialogFragmentViewModel(
    private val addGoalUseCase: AddGoalUseCase,
    private val getRoleUseCase: GetRoleUseCase
) : ViewModel() {

    fun addGoal(goal: Goal) {
        addGoalUseCase.execute(goal = goal)
    }

    fun getRole(roleId: Int): Role {
        return getRoleUseCase.execute(roleId = roleId)
    }
}

class GoalDialogFragmentViewModelFactory(
    private val addGoalUseCase: AddGoalUseCase,
    private val getRoleUseCase: GetRoleUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalDialogFragmentViewModel::class.java)) {
            return GoalDialogFragmentViewModel(
                addGoalUseCase,
                getRoleUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}