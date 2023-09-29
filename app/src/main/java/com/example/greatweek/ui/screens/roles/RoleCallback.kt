package com.example.greatweek.ui.screens.roles

import android.content.Context
import android.view.View
import com.example.greatweek.domain.model.Role

interface RoleCallback {
    fun onMoreClick(v: View, context: Context, role: Role)
    fun onAddGoalClick(role: String)
}