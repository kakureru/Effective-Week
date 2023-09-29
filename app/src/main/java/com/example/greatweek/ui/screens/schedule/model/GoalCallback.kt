package com.example.greatweek.ui.screens.schedule.model

interface GoalCallback {
    fun onCompleteClick(goalId: Int)
    fun onClick(goalId: Int)
}