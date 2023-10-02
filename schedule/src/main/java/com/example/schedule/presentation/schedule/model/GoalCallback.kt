package com.example.schedule.presentation.schedule.model

interface GoalCallback {
    fun onCompleteClick(goalId: Int)
    fun onClick(goalId: Int)
}