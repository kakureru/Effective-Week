package com.example.greatweek.app.presentation.screens.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.model.WeekDay
import com.example.greatweek.domain.usecase.goal.CompleteGoalUseCase
import com.example.greatweek.domain.usecase.goal.DropGoalToRoleUseCase
import com.example.greatweek.domain.usecase.goal.DropGoalToWeekUseCase
import com.example.greatweek.domain.usecase.goal.GetGoalsForDatesUseCase
import com.example.greatweek.domain.usecase.role.DeleteRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesWithGoalsUseCase
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(
    private val getScheduleUseCase: GetGoalsForDatesUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase,
    private val dropGoalToWeekUseCase: DropGoalToWeekUseCase,
    private val deleteRoleUseCase: DeleteRoleUseCase,
    private val getRolesWithGoalsUseCase: GetRolesWithGoalsUseCase,
    private val dropGoalToRoleUseCase: DropGoalToRoleUseCase
) : ViewModel() {

    val preloadDays = 30L
    private val today: LocalDate = LocalDate.now()
    private var startDate = today.minusDays(preloadDays)
    private val endDate = today.plusDays(preloadDays)

    val schedule = getScheduleUseCase.execute(startDate, endDate).map { goals ->
        getDatesBetween(startDate, endDate).map { date ->
            WeekDay(
                date = date,
                goals = goals.filter { it.date == date }
            )
        }
    }.asLiveData()

    val allRoles: LiveData<List<Role>> = getRolesWithGoalsUseCase.execute().asLiveData()

    private var _tabExpanded = MutableLiveData(BottomSheetBehavior.STATE_COLLAPSED)
    val tabExpanded: LiveData<Int> get() = _tabExpanded

    fun collapseBottomSheet() {
        _tabExpanded.value = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun expandBottomSheet() {
        _tabExpanded.value = BottomSheetBehavior.STATE_EXPANDED
    }

    fun completeGoal(goalId: Int) = viewModelScope.launch {
        completeGoalUseCase.execute(goalId = goalId)
    }

    fun dropGoal(goalId: Int, date: LocalDate, isCommitment: Boolean) = viewModelScope.launch {
        dropGoalToWeekUseCase.execute(goalId, date, isCommitment)
    }

    fun dropGoal(goalId: Int, role: String) = viewModelScope.launch {
        dropGoalToRoleUseCase.execute(goalId, role)
    }

    fun deleteRole(name: String) = viewModelScope.launch {
        deleteRoleUseCase.execute(name = name)
    }

    private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate)
        return IntStream.iterate(0) { i -> i + 1 }
            .limit(numOfDaysBetween)
            .mapToObj { i -> startDate.plusDays(i.toLong()) }
            .collect(Collectors.toList())
    }
}