package com.example.schedule.presentation.schedule.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
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
    val havePriorities by remember(priorities) {
        derivedStateOf { priorities.isNotEmpty() }
    }
    val haveAppointments by remember(appointments) {
        derivedStateOf { appointments.isNotEmpty() }
    }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Surface(
            modifier = modifier.width(width),
            shape = MaterialTheme.shapes.medium,
        ) {
            // TODO make lazy
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = weekday,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.alignByBaseline()
                    )
                    Icon(
                        painter = painterResource(id = com.example.core.R.drawable.ic_circle),
                        contentDescription = null,
                        modifier = Modifier
                            .size(7.dp)
                            .alpha(0.5f),
                    )
                    Text(
                        text = date,
                        modifier = Modifier
                            .alignByBaseline()
                            .alpha(0.5f),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                if (!havePriorities && !haveAppointments) {
                    GoalItemPlaceholder(onClick = onAddGoalClick)
                } else {
                    if (havePriorities) {
                        GoalCategoryTitle(
                            text = stringResource(id = R.string.priorities)
                        )
                        for (item in priorities) {
                            goalItem(item)
                        }
                        if (appointments.isNotEmpty())
                            Spacer(modifier = Modifier.height(8.dp))
                    }
                    if (haveAppointments) {
                        GoalCategoryTitle(
                            text = stringResource(id = R.string.appointments)
                        )
                        for (item in appointments) {
                            goalItem(item)
                        }
                    }
                    AddButton(
                        onClick = onAddGoalClick,
                        modifier = Modifier
                            .align(Alignment.End)
                    )
                }

            }
        }
    }
}

@Composable
private fun GoalCategoryTitle(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier
            .padding(bottom = 8.dp)
            .alpha(0.5f)
    )
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

@Preview
@Composable
fun ScheduleDayPreviewNoAppointments() {
    DarkTheme {
        ScheduleDay(
            weekday = "Friday",
            date = "Sep 25",
            isToday = false,
            onAddGoalClick = {},
            priorities = listOf(GoalItem(0, "Sample Goal", "Me"), GoalItem(1, "Sample Goal", "Me")),
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