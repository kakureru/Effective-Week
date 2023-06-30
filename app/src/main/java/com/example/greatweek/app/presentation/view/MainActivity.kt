package com.example.greatweek.app.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.greatweek.R
import com.example.greatweek.app.GreatWeekApplication
import com.example.greatweek.app.presentation.navigation.Screens
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModel
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModelFactory
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject lateinit var navigatorHolder: NavigatorHolder
    @Inject lateinit var router: Router
    private val navigator = AppNavigator(this, R.id.mainContainer)

    @Inject lateinit var scheduleViewModelFactory: ScheduleViewModelFactory
    private lateinit var scheduleViewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as GreatWeekApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        scheduleViewModel = ViewModelProvider(this, scheduleViewModelFactory)[ScheduleViewModel::class.java]

        if (savedInstanceState == null) {
            router.replaceScreen(Screens.Schedule())
        }
    }
    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        router.exit()
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