package com.effectiveweek.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.effectiveweek.app.navigation.RootNavGraph
import com.effectiveweek.app.navigation.Screen
import com.effectiveweek.core.ui.theme.DarkTheme

class MainActivity : ComponentActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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