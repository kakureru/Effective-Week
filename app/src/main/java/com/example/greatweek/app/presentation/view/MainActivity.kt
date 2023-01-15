package com.example.greatweek.app.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.greatweek.R
import com.example.greatweek.app.GreatWeekApplication
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModel
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    @Inject
    lateinit var scheduleViewModelFactory: ScheduleViewModelFactory
    private lateinit var scheduleViewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as GreatWeekApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        scheduleViewModel =
            ViewModelProvider(this, scheduleViewModelFactory)[ScheduleViewModel::class.java]

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }
}