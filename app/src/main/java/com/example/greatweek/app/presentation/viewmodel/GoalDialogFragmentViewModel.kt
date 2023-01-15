package com.example.greatweek.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.usecase.goal.AddGoalUseCase
import com.example.greatweek.domain.usecase.goal.EditGoalUseCase
import com.example.greatweek.domain.usecase.goal.GetGoalUseCase
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesUseCase
import com.example.greatweek.domain.usecase.role.RenameRoleUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

class GoalDialogFragmentViewModel(
    private val addGoalUseCase: AddGoalUseCase,
    private val getRolesUseCase: GetRolesUseCase,
    private val editGoalUseCase: EditGoalUseCase,
    private val getGoalUseCase: GetGoalUseCase
) : ViewModel() {

    private var _id: Int = 0
    val id: Int get() = _id

    private var _title: String = ""
    val title: String get() = _title

    private var _description: String = ""
    val description: String get() = _description

    private var _role: String? = null
    val role: String? get() = _role

    val calendar: Calendar = Calendar.getInstance()

    private var _date: LocalDate? = null
    val date: LocalDate? get() = _date

    private var _time: LocalTime? = null
    val time: LocalTime? get() = _time

    private var _commitment: Boolean = false
    val commitment: Boolean get() = _commitment

    suspend fun getGoal() {
        val goal = getGoalUseCase.execute(goalId = _id)
        _title = goal.title
        _description = goal.description
        _role = goal.role
        _date = goal.date
        _time = goal.time
        _commitment = goal.commitment
        calendar.time = getCalendarTime(date, time)
    }

    fun getRoles(): Flow<List<Role>> {
        return getRolesUseCase.execute()
    }

    fun editGoal() = viewModelScope.launch {
        editGoalUseCase.execute(
            Goal(
                id = id,
                title = title,
                description = description,
                role = role!!,
                date = date,
                time = time,
                commitment = commitment
            )
        )
    }

    fun addGoal() = viewModelScope.launch {
        addGoalUseCase.execute(
            Goal(
                title = title,
                description = description,
                role = role!!,
                date = date,
                time = time,
                commitment = commitment
            )
        )
    }

    fun setId(goalId: Int) {
        _id = goalId
    }

    fun setRole(role: String) {
        _role = role
    }

    fun setGoal(title: String, description: String, commitment: Boolean) {
        _title = title
        _description = description
        _commitment = commitment
    }

    fun setTime(h: Int, m: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, h)
        calendar.set(Calendar.MINUTE, m)
        _time = getLocalTime(calendar)
    }

    fun setDate(y: Int, m: Int, d: Int) {
        calendar.set(Calendar.YEAR, y)
        calendar.set(Calendar.MONTH, m)
        calendar.set(Calendar.DAY_OF_MONTH, d)
        _date = getLocalDate(calendar)
    }

    fun setDate(date: LocalDate) {
        _date = date
        calendar.time = getCalendarTime(date, time)
    }

    private fun getLocalDate(calendar: Calendar): LocalDate? {
        return LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
            .toLocalDate()
    }

    private fun getLocalTime(calendar: Calendar): LocalTime? {
        return LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
            .toLocalTime()
    }

    private fun getCalendarTime(date: LocalDate?, time: LocalTime?): Date {
        val calendarDate = date ?: LocalDate.now()
        val calendarTime = time ?: LocalTime.now()
        return Date.from(
            LocalDateTime.of(calendarDate, calendarTime).atZone(ZoneId.systemDefault()).toInstant()
        )
    }
}

@Suppress("UNCHECKED_CAST")
class GoalDialogFragmentViewModelFactory(
    private val addGoalUseCase: AddGoalUseCase,
    private val getRolesUseCase: GetRolesUseCase,
    private val editGoalUseCase: EditGoalUseCase,
    private val getGoalUseCase: GetGoalUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalDialogFragmentViewModel::class.java)) {
            return GoalDialogFragmentViewModel(
                addGoalUseCase = addGoalUseCase,
                getRolesUseCase = getRolesUseCase,
                editGoalUseCase = editGoalUseCase,
                getGoalUseCase = getGoalUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}