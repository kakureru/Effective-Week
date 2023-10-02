package com.example.schedule.presentation.schedule.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.AddButton
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.R
import com.example.schedule.presentation.schedule.model.GoalItem
import kotlin.math.min

@Composable
fun ScheduleDay(
    weekday: String,
    date: String,
    isToday: Boolean,
    priorities: List<GoalItem>,
    appointments: List<GoalItem>,
    onAddGoalClick: () -> Unit,
    modifier: Modifier = Modifier,
    goalItem: @Composable (GoalItem) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val maxSizeDp = 500
    val width = remember(configuration) { min((configuration.screenWidthDp - 32), maxSizeDp).dp }
    Surface(
        modifier = modifier.width(width),
        shape = MaterialTheme.shapes.medium,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = weekday,
                )
            }
            item {
                Text(
                    text = stringResource(id = R.string.priorities),
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }
            if (priorities.isEmpty()) {
                item {
                    GoalItemPlaceholder()
                }
            }
            items(items = priorities, key = { item -> item.id }) {
                goalItem(it)
            }
            item {
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }
            item {
                Text(text = stringResource(id = R.string.appointments))
            }
            if (appointments.isEmpty()) {
                item {
                    GoalItemPlaceholder()
                }
            }
            items(items = appointments, key = { item -> item.id }) {
                goalItem(it)
            }
            item {
                AddButton(onClick = onAddGoalClick)
            }
        }
    }
}


@Preview
@Composable
fun ScheduleDayPreview() {
    DarkTheme {
        ScheduleDay(
            weekday = "Friday",
            date = "Sep 25",
            isToday = false,
            onAddGoalClick = {},
            priorities = listOf(GoalItem(0, "Sample Goal", "Me"), GoalItem(1, "Sample Goal", "Me")),
            appointments = listOf(GoalItem(2, "Sample Goal", "Me")),
            goalItem = {
                GoalItem(
                    title = it.title,
                    role = it.role,
                    onClick = { },
                    onLongClick = { },
                    onCheck = { },
                )
            }
        )
    }
}


@Preview
@Composable
fun ScheduleDayPreviewNoGoals() {
    DarkTheme {
        ScheduleDay(
            weekday = "Friday",
            date = "Sep 25",
            isToday = false,
            onAddGoalClick = {},
            priorities = emptyList(),
            appointments = emptyList(),
            goalItem = {
                GoalItem(
                    title = it.title,
                    role = it.role,
                    onClick = { },
                    onLongClick = { },
                    onCheck = { },
                )
            }
        )
    }
}