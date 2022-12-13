package com.example.greatweek.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.usecase.goal.AddGoalUseCase
import java.lang.IllegalArgumentException

class GoalDialogFragmentViewModel(
    private val addGoalUseCase: AddGoalUseCase
) : ViewModel() {

    fun addGoal(goal: Goal) {
        addGoalUseCase.execute(goal = goal)
    }
}

class GoalDialogFragmentViewModelFactory(
    private val addGoalUseCase: AddGoalUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalDialogFragmentViewModel::class.java)) {
            return GoalDialogFragmentViewModel(
                addGoalUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}