package com.example.greatweek.app.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.greatweek.R
import com.example.greatweek.app.GreatWeekApplication
import com.example.greatweek.app.presentation.navigation.Screens
import com.example.greatweek.app.presentation.viewmodel.SettingsViewModel
import com.example.greatweek.app.presentation.viewmodel.SettingsViewModelFactory
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(R.layout.activity_settings) {

    @Inject lateinit var navigatorHolder: NavigatorHolder
    @Inject lateinit var router: Router
    private val navigator = AppNavigator(this, R.id.settingsMainContainer)

    @Inject lateinit var settingsViewModelFactory: SettingsViewModelFactory
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as GreatWeekApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        settingsViewModel = ViewModelProvider(this, settingsViewModelFactory)[SettingsViewModel::class.java]
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            router.replaceScreen(Screens.Setting())
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
}

