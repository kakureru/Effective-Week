package com.effectiveweek.schedule.presentation.roles_tab

import com.effectiveweek.schedule.domain.model.Role

internal data class RolesState(
    val roles: List<Role> = emptyList(),
)

internal sealed class RolesNavEvent {
    data object OpenRoleDialog : RolesNavEvent()
    class OpenRoleDialogWithRole(val roleName: String) : RolesNavEvent()
    class OpenGoalDialogWithGoal(val goalId: Int) : RolesNavEvent()
    class OpenGoalDialogWithRole(val roleName: String) : RolesNavEvent()
}

internal sealed class RolesEvent {
    class EditRoleClick(val roleName: String) : RolesEvent()
    class DeleteRoleClick(val roleName: String) : RolesEvent()
    data object AddRoleClick : RolesEvent()
    class GoalDropOnRole(val goalId: Int, val role: String) : RolesEvent()
    class AddGoalToRoleClick(val roleName: String) : RolesEvent()
    class GoalClick(val goalId: Int) : RolesEvent()
    class CompleteGoal(val goalId: Int) : RolesEvent()
}

internal sealed class RolesEffect {
    class Error(val msgResource: Int) : RolesEffect()
}