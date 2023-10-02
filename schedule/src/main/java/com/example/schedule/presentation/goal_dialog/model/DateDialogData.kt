package com.example.schedule.presentation.goal_dialog.model

import android.app.DatePickerDialog.OnDateSetListener

data class DateDialogData(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val onDateSetListener: OnDateSetListener
)