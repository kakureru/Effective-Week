package com.example.greatweek

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.core.ui.theme.DarkTheme
import com.example.greatweek.navigation.RootNavGraph
import com.example.greatweek.navigation.Screen

class MainActivity : ComponentActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, com.example.core.R.color.black)
        setContent {
            DarkTheme {
                RootNavGraph(
                    navHostController = rememberNavController(),
                    startDestination = Screen.Schedule.route
                )
            }
        }
    }
}