package com.example.greatweek.navigation

sealed class Screen(val root: String) {
    object Schedule : Screen(root = "schedule")
}