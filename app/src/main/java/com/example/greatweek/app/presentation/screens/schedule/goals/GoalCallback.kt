package com.example.greatweek.app.presentation.screens.schedule.goals

interface GoalCallback {
    fun onCompleteClick(goalId: Int)
    fun onClick(goalId: Int)
}