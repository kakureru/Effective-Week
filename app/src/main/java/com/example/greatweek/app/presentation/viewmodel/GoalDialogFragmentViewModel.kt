package com.example.greatweek.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.usecase.goal.AddGoalUseCase
import com.example.greatweek.domain.usecase.goal.EditGoalUseCase
import com.example.greatweek.domain.usecase.goal.GetGoalUseCase
import com.example.greatweek.domain.usecase.role.GetRolesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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

    private var _weekday: Int = 0
    val weekday: Int get() = _weekday

    private var _commitment: Boolean = false
    val commitment: Boolean get() = _commitment

    suspend fun getGoal() {
        val goal = getGoalUseCase.execute(goalId = _id)
        _title = goal.title
        _description = goal.description
        _role = goal.role
        _weekday = goal.weekday
        _commitment = goal.commitment
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
                weekday = weekday,
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
                weekday = weekday,
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

    fun setWeekDay(weekDay: Int) {
        _weekday = weekDay
    }

    fun setGoal(title: String, description: String, commitment: Boolean) {
        _title = title
        _description = description
        _commitment = commitment
    }
}