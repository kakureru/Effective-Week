package com.example.schedule.presentation.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.R
import com.example.schedule.domain.repository.GoalRepository
import com.example.schedule.domain.repository.RoleRepository
import com.example.schedule.domain.usecase.GetScheduleForDatesUseCase
import com.example.schedule.domain.usecase.goal.CompleteGoalUseCase
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class ScheduleViewModel(
    private val dropGoalToWeekUseCase: DropGoalToWeekUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase,
    getScheduleForDatesUseCase: GetScheduleForDatesUseCase,
) : ViewModel() {

    private val startDate = getCurrentFirstWeekDay()
    private val endDate = startDate.plusDays(7)

    private val _uiEffect = MutableSharedFlow<ScheduleEffect>()
    val uiEffect: SharedFlow<ScheduleEffect> = _uiEffect.asSharedFlow()

    private val _schedule = getScheduleForDatesUseCase(startDate, endDate).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val uiState: StateFlow<ScheduleState> = _schedule.map { schedule ->
        ScheduleState(
            schedule = schedule.map { it.toScheduleDayItem() },
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000),
        ScheduleState()
    )

    private val _navigationEvents = MutableSharedFlow<ScheduleNavEvent>()
    val navigationEvents: SharedFlow<ScheduleNavEvent> = _navigationEvents.asSharedFlow()

    fun accept(event: ScheduleEvent) {
        when (event) {
            is ScheduleEvent.GoalClick -> onGoalClick(event.goalId)
            is ScheduleEvent.CompleteGoal -> completeGoal(event.goalId)
            is ScheduleEvent.GoalDropOnSchedule -> dropGoalOnSchedule(event.goalId, event.date, event.isAppointment)
            is ScheduleEvent.AddGoalToScheduleDayClick -> onAddGoalToScheduleDayClick(event.epochDay)
        }
    }

    private fun onGoalClick(goalId: Int) = viewModelScope.launch {
        _navigationEvents.emit(ScheduleNavEvent.OpenGoalDialogWithGoal(goalId = goalId))
    }

    private fun onAddGoalToScheduleDayClick(epochDay: Long) = viewModelScope.launch {
        _navigationEvents.emit(ScheduleNavEvent.OpenGoalDialogWithDate(epochDay = epochDay))
    }

    private fun completeGoal(goalId: Int) = viewModelScope.launch {
        completeGoalUseCase(goalId = goalId)
    }

    private fun dropGoalOnSchedule(goalId: Int, date: LocalDate, isCommitment: Boolean) = viewModelScope.launch {
        dropGoalToWeekUseCase(goalId, date, isCommitment)
    }
}