package com.example.greatweek.app.presentation.navigation

import com.example.greatweek.app.presentation.screens.schedule.ScheduleFragment
import com.example.greatweek.app.presentation.screens.settings.SettingsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun Schedule() = FragmentScreen {
        ScheduleFragment()
    }

    fun Setting() = FragmentScreen {
        SettingsFragment()
    }
}