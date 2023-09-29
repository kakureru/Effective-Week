package com.example.greatweek.ui.screens.schedule.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.greatweek.ui.theme.DarkTheme

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
) {
    ScheduleScreenUi(
        schedule = emptyList(),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreenUi(
    schedule: List<Any>,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
    ) { paddingvalues ->
        LazyRow(
            modifier = Modifier.padding(paddingvalues)
        ) {

        }
    }
}

@Preview
@Composable
fun ScheduleScreenUiPreview() {
    DarkTheme {
        ScheduleScreenUi(
            schedule = emptyList()
        )
    }
}