package com.example.greatweek.ui.screens.goaldialog

import com.example.greatweek.ui.screens.goaldialog.dialogdata.DateDialogData
import com.example.greatweek.ui.screens.goaldialog.dialogdata.RoleDialogData
import com.example.greatweek.ui.screens.goaldialog.dialogdata.TimeDialogData

data class GoalDialogState(
    val title: String = "",
    val description: String = "",
    val role: String? = "",
    val date: String? = "",
    val time: String? = "",
    val appointment: Boolean = false,
    val isSuccessful: Boolean = false,
)
//fun Goal.toUI() = GoalData(
//    title = title,
//    description = description,
//    role = role,
//    date = date?.let { DateTimeFormatter.ofPattern("MMM d").format(it) },
//    time = time?.let { DateTimeFormatter.ofPattern("HH:mm").format(it) },
//    commitment = appointment,
//)

sealed class GoalDialogEvent {
    object RoleClick : GoalDialogEvent()
    object TimeClick : GoalDialogEvent()
    object DateClick : GoalDialogEvent()
    object ConfirmClick : GoalDialogEvent()
    class AppointmentCheckChanged(val isChecked: Boolean) : GoalDialogEvent()
    class TitleChanged(val newTitle: String) : GoalDialogEvent()
    class DescriptionChanged(val newDescription: String) : GoalDialogEvent()
}

sealed class GoalDialogEffect {
    class Error(val msg: Int) : GoalDialogEffect()
    class RoleDialog(val dialogData: RoleDialogData) : GoalDialogEffect()
    class TimeDialog(val dialogData: TimeDialogData) : GoalDialogEffect()
    class DateDialog(val dialogData: DateDialogData) : GoalDialogEffect()
}