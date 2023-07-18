package com.example.greatweek.app.presentation.screens.goaldialog.dialogdata

import android.app.TimePickerDialog

data class TimeDialogData(
    val hourOfDay: Int,
    val minute: Int,
    val onTimeSetListener: TimePickerDialog.OnTimeSetListener
)