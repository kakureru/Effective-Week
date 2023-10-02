package com.example.schedule.presentation.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.domain.repository.GoalRepository
import com.example.schedule.domain.repository.RoleRepository
import com.example.schedule.domain.usecase.GetScheduleForDatesUseCase
import com.example.schedule.domain.usecase.goal.DropGoalToRoleUseCase
import com.example.schedule.domain.usecase.goal.DropGoalToWeekUseCase
import com.example.schedule.domain.usecase.role.GetRolesWithGoalsUseCase
import com.example.schedule.presentation.schedule.model.toScheduleDayItem
import com.example.utils.getCurrentFirstWeekDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val roleRepository: RoleRepository,
    private val dropGoalToWeekUseCase: DropGoalToWeekUseCase,
    private val dropGoalToRoleUseCase: DropGoalToRoleUseCase,
    getRolesWithGoalsUseCase: GetRolesWithGoalsUseCase,
    getScheduleForDatesUseCase: GetScheduleForDatesUseCase,
) : ViewModel() {

    private val startDate = getCurrentFirstWeekDay()
    private val endDate = startDate.plusDays(7)

    private val _roles = getRolesWithGoalsUseCase().stateIn(
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

    val uiState: StateFlow<ScheduleState> = combine(
        _roles,
        _schedule,
        _currentDate,
    ) { roles, schedule, currentDate ->
        ScheduleState(
            schedule = schedule.map { it.toScheduleDayItem() },
            roles = roles,
            currentDate = currentDate,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000),
        ScheduleState()
    )

    fun accept(event: ScheduleEvent) {
        when (event) {
            is ScheduleEvent.CompleteGoal -> completeGoal(event.goalId)
            is ScheduleEvent.GoalDropOnSchedule -> dropGoalOnSchedule(event.goalId, event.date, event.isCommitment)
            ScheduleEvent.DragRight -> onDragRight()
            ScheduleEvent.DragLeft -> onDragLeft()
            ScheduleEvent.DragIdle -> Unit
            is ScheduleEvent.DeleteRole -> deleteRole(event.role)
            is ScheduleEvent.GoalDropOnRole -> dropGoalOnRole(event.goalId, event.role)
        }
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

    private fun deleteRole(name: String) = viewModelScope.launch {
        roleRepository.deleteRole(name)
    }

    private fun onDragRight() {
        _currentDate.update { it.plusDays(1) }
    }

    private fun onDragLeft() {
        _currentDate.update { it.minusDays(1) }
    }
}