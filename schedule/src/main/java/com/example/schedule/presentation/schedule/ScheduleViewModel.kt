package com.example.schedule.presentation.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.domain.usecase.GetScheduleForDatesUseCase
import com.example.schedule.domain.usecase.goal.CompleteGoalUseCase
import com.example.schedule.domain.usecase.goal.DropGoalToWeekUseCase
import com.example.schedule.presentation.schedule.model.toScheduleDayItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class ScheduleViewModel(
    private val dropGoalToWeekUseCase: DropGoalToWeekUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase,
    private val getScheduleForDatesUseCase: GetScheduleForDatesUseCase,
) : ViewModel() {

    private val startDate = MutableStateFlow(LocalDate.now())
    private val endDate = MutableStateFlow(LocalDate.now().plusDays(1))

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scheduleFlow = combine(
        startDate,
        endDate,
    ) { start, end ->
        getScheduleForDatesUseCase(start, end)
    }.flatMapLatest { flow ->
        flow.map { schedule ->
            schedule
        }
    }

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = combine(
        _uiState,
        scheduleFlow,
    ) { state, schedule ->
        ScheduleUiState(
            month = state.month,
            schedule = schedule.map { it.toScheduleDayItem() },
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000),
        ScheduleUiState()
    )

    private val _uiEffect = MutableSharedFlow<ScheduleEffect>()
    val uiEffect: SharedFlow<ScheduleEffect> = _uiEffect.asSharedFlow()

    private val _navigationEvents = MutableSharedFlow<ScheduleNavEvent>()
    val navigationEvents: SharedFlow<ScheduleNavEvent> = _navigationEvents.asSharedFlow()

    fun accept(event: ScheduleEvent) {
        when (event) {
            is ScheduleEvent.GoalClick -> onGoalClick(event.goalId)
            is ScheduleEvent.CompleteGoal -> completeGoal(event.goalId)
            is ScheduleEvent.GoalDropOnSchedule -> dropGoalOnSchedule(event.goalId, event.date, event.isAppointment)
            is ScheduleEvent.AddGoalToScheduleDayClick -> onAddGoalToScheduleDayClick(event.epochDay)
            is ScheduleEvent.FirstVisibleDayIndexChange -> onFirstVisibleIndexChange(event.newIndex)
            is ScheduleEvent.LastVisibleDayIndexChange -> onLastVisibleIndexChange(event.newIndex)
            ScheduleEvent.TodayClick -> onTodayClick()
        }
    }

    private fun onTodayClick() = viewModelScope.launch {
        val todayIndex = uiState.value.schedule.indexOfFirst { it.isToday }.takeIf { it > -1 }
        todayIndex?.let {
            _uiEffect.emit(ScheduleEffect.ScrollToDay(todayIndex))
        }
    }

    private fun onFirstVisibleIndexChange(index: Int) {
        val month = uiState.value.schedule.getOrNull(index)?.date?.month?.getDisplayName(TextStyle.FULL, Locale.getDefault())
        month?.let {
            _uiState.update { it.copy(month = month) }
        }
        if (index <= PAGINATION_THRESHOLD) {
            startDate.value = startDate.value.minusDays(7)
        }
    }

    private fun onLastVisibleIndexChange(index: Int) {
        if (index >= uiState.value.schedule.size - PAGINATION_THRESHOLD) {
            endDate.value = endDate.value.plusDays(7)
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

    companion object {
        private const val PAGINATION_THRESHOLD = 7
    }
}