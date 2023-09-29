package com.example.greatweek.ui.screens.schedule.goals

interface GoalCallback {
    fun onCompleteClick(goalId: Int)
    fun onClick(goalId: Int)
}