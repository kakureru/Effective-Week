package com.example.greatweek.app.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}