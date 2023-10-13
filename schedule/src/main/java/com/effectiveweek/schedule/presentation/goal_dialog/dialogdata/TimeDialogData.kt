package com.effectiveweek.schedule.presentation.goal_dialog.dialogdata

import android.app.TimePickerDialog

data class TimeDialogData(
    val hourOfDay: Int,
    val minute: Int,
    val onTimeSetListener: TimePickerDialog.OnTimeSetListener
)