package com.example.schedule.presentation.goal_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greatweek.ui.screens.goaldialog.dialogdata.DateDialogData
import com.example.greatweek.ui.screens.goaldialog.dialogdata.RoleDialogData
import com.example.greatweek.ui.screens.goaldialog.dialogdata.TimeDialogData
import com.example.schedule.R
import com.example.schedule.domain.model.Goal
import com.example.schedule.domain.repository.GoalRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class GoalDialogViewModel (
    private val goalId: Int?,
    initDateEpochDay: Long?,
    initRole: String?,
    private val goalRepository: GoalRepository,
) : ViewModel() {

    private val defaultGoal = Goal(
        id = goalId ?: -1,
        title = "",
        description = "",
        role = initRole,
        date = initDateEpochDay?.takeIf { it > -1 }?.let { LocalDate.ofEpochDay(it) },
        time = null,
        appointment = false,
    )

    private val calendar: Calendar = Calendar.getInstance()
    private val _goalState = MutableStateFlow(defaultGoal)
    private val _isSuccess = MutableStateFlow(false)

    init {
        subscribeToGoalDateTimeUpdates()
        if (goalId != null && goalId > -1) loadGoal(goalId)
    }

    val uiState: StateFlow<GoalDialogState> = combine(
        _goalState,
        _isSuccess,
    ) { goalState, navState ->
        GoalDialogState(
            title = goalState.title,
            description = goalState.description,
            role = goalState.role,
            date = goalState.date?.let { DateTimeFormatter.ofPattern("MMM d").format(it) },
            time = goalState.time?.let { DateTimeFormatter.ofPattern("HH:mm").format(it) },
            appointment = goalState.appointment,
            isSuccessful = navState
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
            GoalDialogEvent.RoleClick -> onRoleClick()
            GoalDialogEvent.TimeClick -> onTimeClick()
            GoalDialogEvent.DateClick -> onDateClick()
            GoalDialogEvent.ConfirmClick -> onConfirmClick()
            is GoalDialogEvent.DescriptionChanged -> onDescriptionChanged(event.newDescription)
            is GoalDialogEvent.TitleChanged -> onTitleChanged(event.newTitle)
            is GoalDialogEvent.AppointmentValueChanged -> onAppointmentChanged(event.isChecked)
        }
    }

    private val isInputCorrect: Boolean get() = with(_goalState.value) {
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

    private fun loadGoal(goalId: Int) = viewModelScope.launch {
        _goalState.value = goalRepository.getGoal(goalId)
    }

    private fun subscribeToGoalDateTimeUpdates() = _goalState
        .onEach { goal ->
            calendar.apply { time = getCalendarTime(goal.date, goal.time) }
        }.launchIn(viewModelScope)

    private fun onTitleChanged(newTitle: String) {
        _goalState.update { it.copy(title = newTitle) }
    }

    private fun onDescriptionChanged(newDescription: String) {
        _goalState.update { it.copy(description = newDescription) }
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
        _goalState.update { it.copy(appointment = isChecked) }
    }

    private suspend fun onConfirmClick() {
        if (isInputCorrect.not())
            _uiEffect.emit(GoalDialogEffect.Error(R.string.error_title_and_role_required))
        else {
            if (goalId == null)
                goalRepository.addGoal(_goalState.value)
            else
                goalRepository.editGoal(_goalState.value)
            _isSuccess.value = true
        }
    }

    private fun setRole(role: String) {
        _goalState.update { it.copy(role = role) }
    }

    private fun setDate(y: Int, m: Int, d: Int) {
        calendar.apply {
            set(Calendar.YEAR, y)
            set(Calendar.MONTH, m)
            set(Calendar.DAY_OF_MONTH, d)
        }
        _goalState.update { it.copy(date = getLocalDate(calendar)) }
    }

    private fun setTime(h: Int, m: Int) {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, h)
            set(Calendar.MINUTE, m)
        }
        _goalState.update { it.copy(time = getLocalTime(calendar)) }
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