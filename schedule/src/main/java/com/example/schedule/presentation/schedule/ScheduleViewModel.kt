package com.example.schedule.presentation.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.domain.model.WeekDay
import com.example.schedule.domain.repository.GoalRepository
import com.example.schedule.domain.repository.RoleRepository
import com.example.schedule.domain.usecase.goal.DropGoalToRoleUseCase
import com.example.schedule.domain.usecase.goal.DropGoalToWeekUseCase
import com.example.schedule.domain.usecase.role.GetRolesWithGoalsUseCase
import com.example.schedule.presentation.schedule.model.toWeekDayItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val dropGoalToWeekUseCase: DropGoalToWeekUseCase,
    private val roleRepository: RoleRepository,
    private val dropGoalToRoleUseCase: DropGoalToRoleUseCase,
    private val getRolesWithGoalsUseCase: GetRolesWithGoalsUseCase,
) : ViewModel() {

    private val _scheduleState = MutableStateFlow(ScheduleState())
    val scheduleState: StateFlow<ScheduleState> = _scheduleState.asStateFlow()

    private val startDate = getFirstWeekDay()
    private val endDate = startDate.plusDays(7)

    private fun getFirstWeekDay(): LocalDate {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            calendar.add(Calendar.DATE, -1);
        return LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId()).toLocalDate()
    }

    init {
        loadSchedule()
        loadRoles()
    }

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

    private fun loadSchedule() = viewModelScope.launch {
        goalRepository.getGoals(startDate, endDate).collect { goals ->
            val schedule = getDatesBetween(startDate, endDate).map { date ->
                WeekDay(
                    date = date,
                    goals = goals.filter { it.date == date }
                )
            }
            _scheduleState.value = ScheduleState(schedule = schedule.map { it.toWeekDayItem() })
        }
    }

    private fun loadRoles() = viewModelScope.launch {
        getRolesWithGoalsUseCase().collect { roles ->
            _scheduleState.update { it.copy(roles = roles) }
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
        _scheduleState.update { it.copy(currentDate = it.currentDate.plusDays(1)) }
    }

    private fun onDragLeft() {
        _scheduleState.update { it.copy(currentDate = it.currentDate.minusDays(1)) }
    }

    private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> =
        IntStream.iterate(0) { i -> i + 1 }
            .limit(ChronoUnit.DAYS.between(startDate, endDate))
            .mapToObj { i -> startDate.plusDays(i.toLong()) }
            .collect(Collectors.toList())

}