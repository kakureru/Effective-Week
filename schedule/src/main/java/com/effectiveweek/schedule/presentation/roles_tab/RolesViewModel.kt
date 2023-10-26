package com.effectiveweek.schedule.presentation.roles_tab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effectiveweek.schedule.R
import com.effectiveweek.schedule.domain.repository.RoleRepository
import com.effectiveweek.schedule.domain.usecase.goal.CompleteGoalUseCase
import com.effectiveweek.schedule.domain.usecase.goal.DropGoalToRoleUseCase
import com.effectiveweek.schedule.domain.usecase.role.GetRolesWithGoalsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class RolesViewModel(
    getRolesWithGoalsUseCase: GetRolesWithGoalsUseCase,
    private val dropGoalToRoleUseCase: DropGoalToRoleUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase,
    private val roleRepository: RoleRepository,
) : ViewModel() {

    private val _uiEffect = MutableSharedFlow<RolesEffect>()
    val uiEffect: SharedFlow<RolesEffect> = _uiEffect.asSharedFlow()

    private val _navigationEvents = MutableSharedFlow<RolesNavEvent>()
    val navigationEvents: SharedFlow<RolesNavEvent> = _navigationEvents.asSharedFlow()

    private val _rolesWithGoals = getRolesWithGoalsUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val uiState: StateFlow<RolesState> = _rolesWithGoals.map { roles ->
        RolesState(
            roles = roles
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        RolesState()
    )

    fun accept(event: RolesEvent) {
        when (event) {
            is RolesEvent.GoalClick -> onGoalClick(event.goalId)
            is RolesEvent.CompleteGoal -> completeGoal(event.goalId)
            is RolesEvent.DeleteRoleClick -> onDeleteRoleClick(event.roleName)
            is RolesEvent.EditRoleClick -> onEditRoleClick(event.roleName)
            is RolesEvent.GoalDropOnRole -> dropGoalOnRole(event.goalId, event.role)
            is RolesEvent.AddGoalToRoleClick -> onAddGoalToRoleClick(event.roleName)
            RolesEvent.AddRoleClick -> onAddRoleClick()
        }
    }

    private fun onGoalClick(goalId: Int) = viewModelScope.launch {
        _navigationEvents.emit(RolesNavEvent.OpenGoalDialogWithGoal(goalId = goalId))
    }

    private fun completeGoal(goalId: Int) = viewModelScope.launch {
        completeGoalUseCase(goalId = goalId)
    }

    private fun onDeleteRoleClick(roleName: String) = viewModelScope.launch {
        val roleGoals = _rolesWithGoals.value.find { it.name == roleName }?.goals ?: return@launch
        if (roleGoals.isNotEmpty())
            _uiEffect.emit(RolesEffect.Error(R.string.error_cant_delete_role_with_active_goals))
        else
            roleRepository.deleteRole(roleName)
    }

    private fun onEditRoleClick(roleName: String) = viewModelScope.launch {
        _navigationEvents.emit(RolesNavEvent.OpenRoleDialogWithRole(roleName = roleName))
    }

    private fun onAddRoleClick() = viewModelScope.launch {
        _navigationEvents.emit(RolesNavEvent.OpenRoleDialog)
    }

    private fun onAddGoalToRoleClick(roleName: String) = viewModelScope.launch {
        _navigationEvents.emit(RolesNavEvent.OpenGoalDialogWithRole(roleName = roleName))
    }

    private fun dropGoalOnRole(goalId: Int, role: String) = viewModelScope.launch {
        dropGoalToRoleUseCase(goalId, role)
    }
}