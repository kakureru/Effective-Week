package com.example.schedule.presentation.schedule.model

import android.content.Context
import android.view.View
import com.example.schedule.domain.model.Role

interface RoleCallback {
    fun onMoreClick(v: View, context: Context, role: Role)
    fun onAddGoalClick(role: String)
}