package com.example.schedule.presentation.goal_dialog

import com.example.greatweek.ui.screens.goaldialog.dialogdata.DateDialogData
import com.example.greatweek.ui.screens.goaldialog.dialogdata.TimeDialogData
import com.example.schedule.presentation.role_pick_dialog.RoleItem
import java.time.LocalTime

data class GoalDialogState(
    val title: String = "",
    val description: String = "",
    val role: String? = "",
    val availableRoles: List<RoleItem> = emptyList(),
    val date: String? = "",
    val timePrint: String? = "",
    val time: LocalTime = LocalTime.MIDNIGHT,
    val appointment: Boolean = false,
    val navState: GoalDialogNavState = GoalDialogNavState.Idle,
)

sealed class GoalDialogNavState {
    object Idle : GoalDialogNavState()
    object Dismiss : GoalDialogNavState()
}

sealed class GoalDialogEvent {
    class RolePick(val roleName: String) : GoalDialogEvent()
    class TimePick(val hour: Int, val minute: Int) : GoalDialogEvent()
    class DatePick(val dateMillis: Long) : GoalDialogEvent()
    object ConfirmClick : GoalDialogEvent()
    class AppointmentValueChanged(val isChecked: Boolean) : GoalDialogEvent()
    class TitleChanged(val newTitle: String) : GoalDialogEvent()
    class DescriptionChanged(val newDescription: String) : GoalDialogEvent()
}

sealed class GoalDialogEffect {
    class Error(val msg: Int) : GoalDialogEffect()
    class TimeDialog(val dialogData: TimeDialogData) : GoalDialogEffect()
    class DateDialog(val dialogData: DateDialogData) : GoalDialogEffect()
}