package com.example.schedule.presentation.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.R
import com.example.schedule.domain.repository.GoalRepository
import com.example.schedule.domain.repository.RoleRepository
import com.example.schedule.domain.usecase.GetScheduleForDatesUseCase
import com.example.schedule.domain.usecase.goal.DropGoalToRoleUseCase
import com.example.schedule.domain.usecase.goal.DropGoalToWeekUseCase
import com.example.schedule.domain.usecase.role.GetRolesWithGoalsUseCase
import com.example.schedule.presentation.schedule.model.toScheduleDayItem
import com.example.utils.getCurrentFirstWeekDay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class ScheduleViewModel(
    private val goalRepository: GoalRepository,
    private val roleRepository: RoleRepository,
    private val dropGoalToWeekUseCase: DropGoalToWeekUseCase,
    private val dropGoalToRoleUseCase: DropGoalToRoleUseCase,
    getRolesWithGoalsUseCase: GetRolesWithGoalsUseCase,
    getScheduleForDatesUseCase: GetScheduleForDatesUseCase,
) : ViewModel() {

    private val startDate = getCurrentFirstWeekDay()
    private val endDate = startDate.plusDays(7)

    private val _uiEffect = MutableSharedFlow<ScheduleEffect>()
    val uiEffect: SharedFlow<ScheduleEffect> = _uiEffect.asSharedFlow()

    private val _rolesWithGoals = getRolesWithGoalsUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
    private val _schedule = getScheduleForDatesUseCase(startDate, endDate).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
    private val _currentDate = MutableStateFlow(LocalDate.now())
    private val _navState = MutableStateFlow<ScheduleNavState>(ScheduleNavState.Idle)

    val uiState: StateFlow<ScheduleState> = combine(
        _rolesWithGoals,
        _schedule,
        _currentDate,
        _navState,
    ) { roles, schedule, currentDate, navState ->
        ScheduleState(
            schedule = schedule.map { it.toScheduleDayItem() },
            roles = roles,
            currentDate = currentDate,
            navState = navState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000),
        ScheduleState()
    )

    fun accept(event: ScheduleEvent) {
        when (event) {
            ScheduleEvent.DragRight -> onDragRight()
            ScheduleEvent.DragLeft -> onDragLeft()
            ScheduleEvent.DragIdle -> Unit
            is ScheduleEvent.GoalClick -> onGoalClick(event.goalId)
            is ScheduleEvent.CompleteGoal -> completeGoal(event.goalId)
            is ScheduleEvent.GoalDropOnSchedule -> dropGoalOnSchedule(event.goalId, event.date, event.isAppointment)
            is ScheduleEvent.GoalDropOnRole -> dropGoalOnRole(event.goalId, event.role)
            is ScheduleEvent.AddGoalToScheduleDayClick -> onAddGoalToScheduleDayClick(event.epochDay)
            is ScheduleEvent.AddGoalToRoleClick -> onAddGoalToRoleClick(event.roleName)
            ScheduleEvent.AddRoleClick -> onAddRoleClick()
            is ScheduleEvent.DeleteRoleClick -> onDeleteRoleClick(event.roleName)
            is ScheduleEvent.EditRoleClick -> onEditRoleClick(event.roleName)
        }
    }

    private fun onGoalClick(goalId: Int) {
        _navState.value = ScheduleNavState.OpenGoalDialogWithGoal(goalId = goalId)
    }

    private fun onAddGoalToScheduleDayClick(epochDay: Long) {
        _navState.value = ScheduleNavState.OpenGoalDialogWithDate(epochDay = epochDay)
    }

    private fun onAddGoalToRoleClick(roleName: String) {
        _navState.value = ScheduleNavState.OpenGoalDialogWithRole(roleName = roleName)
    }

    private fun onAddRoleClick() {
        _navState.value = ScheduleNavState.OpenRoleDialog
    }

    private fun onDeleteRoleClick(roleName: String) = viewModelScope.launch {
        val roleGoals = _rolesWithGoals.value.find { it.name == roleName }?.goals ?: return@launch
        if (roleGoals.isNotEmpty())
            _uiEffect.emit(ScheduleEffect.Error(R.string.error_cant_delete_role_with_active_goals))
        else
            roleRepository.deleteRole(roleName)
    }

    private fun onEditRoleClick(roleName: String) {
        _navState.value = ScheduleNavState.OpenRoleDialogWithRole(roleName = roleName)
    }

    private fun completeGoal(goalId: Int) = viewModelScope.launch {
        goalRepository.completeGoal(goalId = goalId)
    }

    private fun dropGoalOnSchedule(goalId: Int, date: LocalDate, isCommitment: Boolean) = viewModelScope.launch {
        dropGoalToWeekUseCase(goalId, date, isCommitment)
    }

    private fun dropGoalOnRole(goalId: Int, role: String) = viewModelScope.launch {
        dropGoalToRoleUseCase(goalId, role)
    }

    private fun onDragRight() {
        _currentDate.update { it.plusDays(1) }
    }

    private fun onDragLeft() {
        _currentDate.update { it.minusDays(1) }
    }
}