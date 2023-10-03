package com.example.greatweek.ui.screens.goaldialog.dialogdata

import android.app.DatePickerDialog.OnDateSetListener

data class DateDialogData(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val onDateSetListener: OnDateSetListener
)