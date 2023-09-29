package com.example.greatweek.navigation

import com.example.greatweek.ui.screens.schedule.ScheduleFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun Schedule() = FragmentScreen {
        ScheduleFragment()
    }
}