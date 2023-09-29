package com.example.greatweek.ui.screens.schedule.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greatweek.R
import com.example.greatweek.ui.core.AddGoalButton
import com.example.greatweek.ui.screens.schedule.goals.GoalCallback
import com.example.greatweek.ui.screens.schedule.model.GoalItem
import com.example.greatweek.ui.theme.DarkTheme

@Composable
fun ScheduleDay(
    weekday: String,
    date: String,
    isToday: Boolean,
    goalCallback: GoalCallback,
    priorities: List<GoalItem>,
    appointments: List<GoalItem>,
    onAddGoalClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.width(340.dp),
        shape = MaterialTheme.shapes.medium
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
                GoalItem(
                    title = it.title,
                    role = it.role,
                    onClick = { goalCallback.onClick(it.id) },
                    onLongClick = { },
                    onCheck = { goalCallback.onCompleteClick(it.id) },
                )
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
                GoalItem(
                    title = it.title,
                    role = it.role,
                    onClick = { goalCallback.onClick(it.id) },
                    onLongClick = { },
                    onCheck = { goalCallback.onCompleteClick(it.id) },
                )
            }
            item {
                AddGoalButton(onClick = onAddGoalClick)
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
            goalCallback = previewGoalCallback
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
            goalCallback = previewGoalCallback
        )
    }
}

private val previewGoalCallback = object : GoalCallback {
    override fun onCompleteClick(goalId: Int) = Unit
    override fun onClick(goalId: Int) = Unit
}