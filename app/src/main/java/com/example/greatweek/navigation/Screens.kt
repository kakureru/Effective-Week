package com.example.greatweek.navigation

import com.example.schedule.presentation.schedule.ScheduleFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun Schedule() = FragmentScreen {
        ScheduleFragment()
    }
}