package com.example.greatweek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.core.ui.theme.DarkTheme
import com.example.greatweek.navigation.RootNavGraph
import com.example.greatweek.navigation.Screen

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