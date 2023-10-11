package com.example.schedule.presentation.roles_tab

import com.example.schedule.domain.model.Role

data class RolesState(
    val roles: List<Role> = emptyList(),
)

sealed class RolesNavEvent {
    data object OpenRoleDialog : RolesNavEvent()
    class OpenRoleDialogWithRole(val roleName: String) : RolesNavEvent()
    class OpenGoalDialogWithGoal(val goalId: Int) : RolesNavEvent()
    class OpenGoalDialogWithRole(val roleName: String) : RolesNavEvent()
}

sealed class RolesEvent {
    class EditRoleClick(val roleName: String) : RolesEvent()
    class DeleteRoleClick(val roleName: String) : RolesEvent()
    data object AddRoleClick : RolesEvent()
    class GoalDropOnRole(val goalId: Int, val role: String) : RolesEvent()
    class AddGoalToRoleClick(val roleName: String) : RolesEvent()
    class GoalClick(val goalId: Int) : RolesEvent()
    class CompleteGoal(val goalId: Int) : RolesEvent()
}

sealed class RolesEffect {
    class Error(val msgResource: Int) : RolesEffect()
}