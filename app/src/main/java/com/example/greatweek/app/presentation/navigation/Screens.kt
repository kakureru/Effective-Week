package com.example.greatweek.app.presentation.navigation

import com.example.greatweek.app.presentation.view.ScheduleFragment
import com.example.greatweek.app.presentation.view.SettingsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun Schedule() = FragmentScreen {
        ScheduleFragment()
    }

    fun Setting() = FragmentScreen {
        SettingsFragment()
    }
}