package com.effectiveweek.schedule.presentation.goal_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effectiveweek.schedule.R
import com.effectiveweek.schedule.domain.model.Goal
import com.effectiveweek.schedule.domain.model.Role
import com.effectiveweek.schedule.domain.repository.GoalRepository
import com.effectiveweek.schedule.domain.repository.RoleRepository
import com.effectiveweek.schedule.presentation.role_pick_dialog.toRoleItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal class GoalDialogViewModel (
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

    private val _roles: StateFlow<List<Role>> = roleRepository.getRoles().stateInViewModel(emptyList())
    private val _goalState = MutableStateFlow(defaultGoal)

    init {
        if (goalId > -1) loadGoal(goalId)
    }

    private val _uiState = MutableStateFlow(GoalDialogState())
    val uiState: StateFlow<GoalDialogState> = combine(
        _goalState,
        _uiState,
        _roles,
    ) { goalState, uiState, roles ->
        GoalDialogState(
            role = goalState.role,
            availableRoles = roles.map { it.toRoleItem() },
            date = goalState.date?.let { DateTimeFormatter.ofPattern("MMM d").format(it) },
            timePrint = goalState.time?.let { DateTimeFormatter.ofPattern("HH:mm").format(it) },
            appointment = goalState.appointment,
            navState = uiState.navState,
            isAddingDescription = if (goalState.description.isNotBlank()) true else uiState.isAddingDescription
        )
    }.stateInViewModel(GoalDialogState())

    val titleState: StateFlow<GoalDialogTitleState> = _goalState.map {
        GoalDialogTitleState(
            text = it.title
        )
    }.stateInViewModel(GoalDialogTitleState())

    val descriptionState: StateFlow<GoalDialogDescriptionState> = _goalState.map {
        GoalDialogDescriptionState(
            text = it.description
        )
    }.stateInViewModel(GoalDialogDescriptionState())

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
            GoalDialogEvent.DescriptionPickerClick -> onDescriptionPickerClick()
        }
    }

    private fun onDescriptionPickerClick() {
        _uiState.update { it.copy(isAddingDescription = true) }
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
        if (_goalState.value.title.isBlank()) {
            _uiEffect.emit(GoalDialogEffect.Error(R.string.error_title_is_required))
            return
        }
        if (_goalState.value.role == null) {
            _uiEffect.emit(GoalDialogEffect.ShowRolePickDialog)
            return
        }
        if (goalId == -1)
            goalRepository.addGoal(_goalState.value)
        else
            goalRepository.editGoal(_goalState.value)
        _uiState.update { it.copy(navState = GoalDialogNavState.Dismiss) }
    }

    private fun <T> Flow<T>.stateInViewModel(
        initialValue: T,
        scope: CoroutineScope = viewModelScope,
        started: SharingStarted = SharingStarted.WhileSubscribed(5000)
    ): StateFlow<T> = stateIn(scope, started, initialValue)
}