package com.example.greatweek.app.presentation.screens.roles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greatweek.domain.repository.GoalRepository
import com.example.greatweek.domain.repository.RoleRepository
import com.example.greatweek.domain.usecase.goal.DropGoalToRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesWithGoalsUseCase
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class RolesViewModel @Inject constructor(
    private val roleRepository: RoleRepository,
    private val goalRepository: GoalRepository,
    private val getRolesWithGoalsUseCase: GetRolesWithGoalsUseCase,
    private val dropGoalToRoleUseCase: DropGoalToRoleUseCase,
) : ViewModel() {

    private val _rolesState = MutableStateFlow(RolesState())
    val rolesState: StateFlow<RolesState> = _rolesState.asStateFlow()

    fun accept(event: RolesEvent) {
        when (event) {
            is RolesEvent.CompleteGoal -> completeGoal(event.goalId)
            is RolesEvent.GoalDrop -> dropGoal(event.goalId, event.role)
            is RolesEvent.DeleteRole -> deleteRole(event.role)
            RolesEvent.Collapse -> collapseBottomSheet()
            RolesEvent.Expand -> expandBottomSheet()
        }
    }

    init {
        loadRoles()
    }

    private fun loadRoles() = viewModelScope.launch {
        getRolesWithGoalsUseCase().collect { roles ->
            _rolesState.update { it.copy(roles = roles) }
        }
    }

    private fun collapseBottomSheet() {
        _rolesState.update { it.copy(expanded = BottomSheetBehavior.STATE_COLLAPSED) }
    }

    private fun expandBottomSheet() {
        _rolesState.update { it.copy(expanded = BottomSheetBehavior.STATE_EXPANDED) }
    }

    private fun completeGoal(goalId: Int) = viewModelScope.launch {
        goalRepository.completeGoal(goalId = goalId)
    }

    private fun dropGoal(goalId: Int, role: String) = viewModelScope.launch {
        dropGoalToRoleUseCase(goalId, role)
    }

    private fun deleteRole(name: String) = viewModelScope.launch {
        roleRepository.deleteRole(name)
    }
}