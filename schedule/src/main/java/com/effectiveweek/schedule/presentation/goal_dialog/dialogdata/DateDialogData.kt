package com.effectiveweek.schedule.presentation.goal_dialog.dialogdata

import android.app.DatePickerDialog.OnDateSetListener

data class DateDialogData(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val onDateSetListener: OnDateSetListener
)