package com.example.greatweek.app.presentation.viewmodel

import androidx.lifecycle.*
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.model.WeekDay
import com.example.greatweek.domain.usecase.goal.CompleteGoalUseCase
import com.example.greatweek.domain.usecase.goal.DropGoalToRoleUseCase
import com.example.greatweek.domain.usecase.goal.DropGoalToWeekUseCase
import com.example.greatweek.domain.usecase.goal.GetWeekUseCase
import com.example.greatweek.domain.usecase.role.DeleteRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesWithGoalsUseCase
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val getWeekUseCase: GetWeekUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase,
    private val dropGoalToWeekUseCase: DropGoalToWeekUseCase,
    private val deleteRoleUseCase: DeleteRoleUseCase,
    private val getRolesWithGoalsUseCase: GetRolesWithGoalsUseCase,
    private val dropGoalToRoleUseCase: DropGoalToRoleUseCase
): ViewModel() {

    val week: LiveData<List<WeekDay>> = getWeekUseCase.execute().asLiveData()

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

    fun dropGoal(goalId: Int, date: Int, isCommitment: Boolean) = viewModelScope.launch {
        dropGoalToWeekUseCase.execute(goalId, date, isCommitment)
    }

    fun dropGoal(goalId: Int, role: String) = viewModelScope.launch {
        dropGoalToRoleUseCase.execute(goalId, role)
    }

    fun deleteRole(name: String) = viewModelScope.launch {
        deleteRoleUseCase.execute(name = name)
    }
}