package com.effectiveweek.schedule.presentation.goal_dialog

import com.effectiveweek.schedule.presentation.role_pick_dialog.RoleItem
import java.time.LocalTime

internal data class GoalDialogState(
    val role: String? = "",
    val availableRoles: List<RoleItem> = emptyList(),
    val date: String? = "",
    val timePrint: String? = "",
    val time: LocalTime = LocalTime.MIDNIGHT,
    val appointment: Boolean = false,
    val isAddingDescription: Boolean = false,
    val navState: GoalDialogNavState = GoalDialogNavState.Idle,
)

internal data class GoalDialogTitleState(
    val text: String = ""
)

internal data class GoalDialogDescriptionState(
    val text: String = ""
)

internal sealed class GoalDialogNavState {
    object Idle : GoalDialogNavState()
    object Dismiss : GoalDialogNavState()
}

internal sealed class GoalDialogEvent {
    class RolePick(val roleName: String) : GoalDialogEvent()
    class TimePick(val hour: Int, val minute: Int) : GoalDialogEvent()
    class DatePick(val dateMillis: Long) : GoalDialogEvent()
    object DescriptionPickerClick : GoalDialogEvent()
    object ConfirmClick : GoalDialogEvent()
    object IsAppointmentClick : GoalDialogEvent()
    class TitleChanged(val newTitle: String) : GoalDialogEvent()
    class DescriptionChanged(val newDescription: String) : GoalDialogEvent()
}

internal sealed class GoalDialogEffect {
    class Error(val msgResource: Int) : GoalDialogEffect()
    object ShowRolePickDialog : GoalDialogEffect()
}