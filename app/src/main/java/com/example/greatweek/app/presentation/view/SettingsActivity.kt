package com.example.greatweek.app.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.greatweek.R
import com.example.greatweek.app.GreatWeekApplication
import com.example.greatweek.app.presentation.viewmodel.SettingsViewModel
import com.example.greatweek.app.presentation.viewmodel.SettingsViewModelFactory
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(R.layout.activity_settings) {

    private lateinit var navController: NavController

    @Inject
    lateinit var settingsViewModelFactory: SettingsViewModelFactory
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as GreatWeekApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        settingsViewModel =
            ViewModelProvider(this, settingsViewModelFactory)[SettingsViewModel::class.java]

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

