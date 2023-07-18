package com.example.greatweek.app.presentation.screens.goaldialog

import com.example.greatweek.app.presentation.screens.goaldialog.dialogdata.DateDialogData
import com.example.greatweek.app.presentation.screens.goaldialog.dialogdata.RoleDialogData
import com.example.greatweek.app.presentation.screens.goaldialog.dialogdata.TimeDialogData
import com.example.greatweek.domain.model.Goal
import java.time.format.DateTimeFormatter

data class GoalDialogState(
    val goalData: GoalData,
    val isSuccessful: Boolean = false,
)

data class GoalData(
    val title: String,
    val description: String,
    val role: String?,
    val date: String?,
    val time: String?,
    val commitment: Boolean,
)

fun Goal.toUI() = GoalData(
    title = title,
    description = description,
    role = role,
    date = date?.let { DateTimeFormatter.ofPattern("MMM d").format(it) },
    time = time?.let { DateTimeFormatter.ofPattern("HH:mm").format(it) },
    commitment = appointment,
)

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