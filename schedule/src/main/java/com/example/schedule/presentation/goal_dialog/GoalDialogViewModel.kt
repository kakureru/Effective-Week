package com.example.schedule.presentation.goal_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.R
import com.example.schedule.domain.model.Goal
import com.example.schedule.domain.model.Role
import com.example.schedule.domain.repository.GoalRepository
import com.example.schedule.domain.repository.RoleRepository
import com.example.schedule.presentation.role_pick_dialog.toRoleItem
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
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class GoalDialogViewModel (
    private val goalId: Int,
    initDateEpochDay: Long?,
    initRole: String?,
    roleRepository: RoleRepository,
    private val goalRepository: GoalRepository,
) : ViewModel() {

    private val defaultGoal = Goal(
        id = goalId,
        title = "",
        description = "",
        role = initRole,
        date = initDateEpochDay?.takeIf { it > -1 }?.let { LocalDate.ofEpochDay(it) },
        time = null,
        appointment = false,
    )

    private val _roles: StateFlow<List<Role>> = roleRepository.getRoles().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000L),
        emptyList()
    )
    private val _goalState = MutableStateFlow(defaultGoal)
    private val _navState = MutableStateFlow<GoalDialogNavState>(GoalDialogNavState.Idle)

    init {
        if (goalId > -1) loadGoal(goalId)
    }

    val uiState: StateFlow<GoalDialogState> = combine(
        _goalState,
        _navState,
        _roles,
    ) { goalState, navState, roles ->
        GoalDialogState(
            title = goalState.title,
            description = goalState.description,
            role = goalState.role,
            availableRoles = roles.map { it.toRoleItem() },
            date = goalState.date?.let { DateTimeFormatter.ofPattern("MMM d").format(it) },
            timePrint = goalState.time?.let { DateTimeFormatter.ofPattern("HH:mm").format(it) },
            appointment = goalState.appointment,
            navState = navState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000L),
        GoalDialogState()
    )

    private val _uiEffect = MutableSharedFlow<GoalDialogEffect>()
    val uiEffect: SharedFlow<GoalDialogEffect> = _uiEffect.asSharedFlow()

    fun accept(event: GoalDialogEvent) = viewModelScope.launch {
        when(event) {
            is GoalDialogEvent.RolePick -> onRolePick(event.roleName)
            is GoalDialogEvent.TimePick -> onTimePick(event.hour, event.minute)
            is GoalDialogEvent.DatePick -> onDatePick(event.dateMillis)
            GoalDialogEvent.ConfirmClick -> onConfirmClick()
            is GoalDialogEvent.DescriptionChanged -> onDescriptionChanged(event.newDescription)
            is GoalDialogEvent.TitleChanged -> onTitleChanged(event.newTitle)
            GoalDialogEvent.IsAppointmentClick -> onAppointmentChanged()
        }
    }

    private val isInputCorrect: Boolean get() = with(_goalState.value) {
        title.isNotEmpty() && role != null
    }

    private fun loadGoal(goalId: Int) = viewModelScope.launch {
        _goalState.value = goalRepository.getGoal(goalId)
    }

    private fun onTitleChanged(newTitle: String) {
        _goalState.update { it.copy(title = newTitle) }
    }

    private fun onDescriptionChanged(newDescription: String) {
        _goalState.update { it.copy(description = newDescription) }
    }

    private fun onRolePick(roleName: String) {
        _goalState.update { it.copy(role = roleName) }
    }

    private fun onDatePick(dateMillis: Long) {
        _goalState.update { it.copy(date = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()) }
    }

    private fun onTimePick(hour: Int, minute: Int) {
        _goalState.update { it.copy(time = LocalTime.of(hour, minute)) }
    }

    private fun onAppointmentChanged() {
        _goalState.update { it.copy(appointment = !it.appointment) }
    }

    private suspend fun onConfirmClick() {
        if (isInputCorrect.not())
            _uiEffect.emit(GoalDialogEffect.Error(R.string.error_title_and_role_required))
        else {
            if (goalId == -1)
                goalRepository.addGoal(_goalState.value)
            else
                goalRepository.editGoal(_goalState.value)
            _navState.value = GoalDialogNavState.Dismiss
        }
    }
}