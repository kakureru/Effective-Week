package com.example.greatweek.app.presentation.screens.roles

import com.example.greatweek.domain.model.Role
import com.google.android.material.bottomsheet.BottomSheetBehavior

data class RolesState(
    val roles: List<Role> = emptyList(),
    val expanded: Int = BottomSheetBehavior.STATE_COLLAPSED, // TODO rename
)

sealed class RolesEvent {
    class DeleteRole(val role: String) : RolesEvent()
    class CompleteGoal(val goalId: Int) : RolesEvent()
    class GoalDrop(val goalId: Int, val role: String) : RolesEvent()
    object Collapse : RolesEvent()
    object Expand : RolesEvent()
}