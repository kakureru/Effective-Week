package com.example.schedule.presentation.goal_dialog.model

import android.app.TimePickerDialog

data class TimeDialogData(
    val hourOfDay: Int,
    val minute: Int,
    val onTimeSetListener: TimePickerDialog.OnTimeSetListener
)