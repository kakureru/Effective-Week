package com.example.schedule.presentation.goal_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.R
import com.example.schedule.domain.model.Goal
import com.example.schedule.domain.repository.GoalRepository
import com.example.schedule.presentation.goal_dialog.model.DateDialogData
import com.example.schedule.presentation.goal_dialog.model.RoleDialogData
import com.example.schedule.presentation.goal_dialog.model.TimeDialogData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

class GoalDialogViewModel (
    private val goalId: Int?,
    private val initDate: LocalDate?,
    private val initRole: String?,
    private val goalRepository: GoalRepository,
) : ViewModel() {

    private val defaultGoal = Goal(
        id = -1,
        title = "",
        description = "",
        role = initRole,
        date = initDate,
        time = null,
        appointment = false,
    )

    private val calendar: Calendar = Calendar.getInstance()
    private val goalState = MutableStateFlow(defaultGoal)

    private val _uiState = MutableStateFlow(GoalDialogState(/*goalState.value.toUI()*/))
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<GoalDialogEffect>()
    val uiEffect: SharedFlow<GoalDialogEffect> = _uiEffect.asSharedFlow()

    private val isInputCorrect: Boolean get() = with(goalState.value) {
        title.isNotEmpty() && role != null
    }

    private val roleDialogData get() = RoleDialogData { role ->
        setRole(role)
    }

    private val dateDialogData get() = DateDialogData(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH),
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
    ) { _, y, m, d ->
        setDate(y, m, d)
    }

    private val timeDialogData get() = TimeDialogData(
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY),
        minute = calendar.get(Calendar.MINUTE),
    ) { _, h, m ->
        setTime(h, m)
    }

    fun accept(event: GoalDialogEvent) = viewModelScope.launch {
        when(event) {
            GoalDialogEvent.RoleClick -> onRoleClick()
            GoalDialogEvent.TimeClick -> onTimeClick()
            GoalDialogEvent.DateClick -> onDateClick()
            GoalDialogEvent.ConfirmClick -> onConfirmClick()
            is GoalDialogEvent.DescriptionChanged -> onDescriptionChanged(event.newDescription)
            is GoalDialogEvent.TitleChanged -> onTitleChanged(event.newTitle)
            is GoalDialogEvent.AppointmentCheckChanged -> onAppointmentChanged(event.isChecked)
        }
    }

    init {
        subscribeToGoalState()
        if (goalId != null) loadGoal(goalId)
    }

    private fun subscribeToGoalState() = goalState
        .onEach { goal ->
//            _uiState.update { it.copy(goalData = goal.toUI()) }
            calendar.apply { time = getCalendarTime(goal.date, goal.time) }
        }.launchIn(viewModelScope)

    private fun loadGoal(goalId: Int) = viewModelScope.launch {
        goalState.value = goalRepository.getGoal(goalId)
    }

    private fun onTitleChanged(newTitle: String) {
        goalState.update { it.copy(title = newTitle) }
    }

    private fun onDescriptionChanged(newDescription: String) {
        goalState.update { it.copy(description = newDescription) }
    }

    private suspend fun onRoleClick() {
        _uiEffect.emit(GoalDialogEffect.RoleDialog(roleDialogData))
    }

    private suspend fun onDateClick() {
        _uiEffect.emit(GoalDialogEffect.DateDialog(dateDialogData))
    }

    private suspend fun onTimeClick() {
        _uiEffect.emit(GoalDialogEffect.TimeDialog(timeDialogData))
    }

    private fun onAppointmentChanged(isChecked: Boolean) {
        goalState.update { it.copy(appointment = isChecked) }
    }

    private suspend fun onConfirmClick() {
        if (isInputCorrect.not())
            _uiEffect.emit(GoalDialogEffect.Error(R.string.error_title_and_role_required))
        else {
            if (goalId == null)
                goalRepository.addGoal(goalState.value)
            else
                goalRepository.editGoal(goalState.value)
            _uiState.update { it.copy(isSuccessful = true) }
        }
    }

    private fun setRole(role: String) {
        goalState.update { it.copy(role = role) }
    }

    private fun setDate(y: Int, m: Int, d: Int) {
        calendar.apply {
            set(Calendar.YEAR, y)
            set(Calendar.MONTH, m)
            set(Calendar.DAY_OF_MONTH, d)
        }
        goalState.update { it.copy(date = getLocalDate(calendar)) }
    }

    private fun setTime(h: Int, m: Int) {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, h)
            set(Calendar.MINUTE, m)
        }
        goalState.update { it.copy(time = getLocalTime(calendar)) }
    }

    private fun getLocalDate(calendar: Calendar): LocalDate? =
        LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId()).toLocalDate()

    private fun getLocalTime(calendar: Calendar): LocalTime? =
        LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId()).toLocalTime()

    private fun getCalendarTime(date: LocalDate?, time: LocalTime?): Date {
        val calendarDate = date ?: LocalDate.now()
        val calendarTime = time ?: LocalTime.now()
        return Date.from(
            LocalDateTime.of(calendarDate, calendarTime).atZone(ZoneId.systemDefault()).toInstant()
        )
    }
}