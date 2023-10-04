package com.example.schedule.presentation.schedule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.AddButton
import com.example.core.ui.draganddrop.DragAndDropState
import com.example.core.ui.draganddrop.DragSurface
import com.example.core.ui.draganddrop.DropSurface
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.R
import com.example.schedule.presentation.schedule.model.GoalItem
import com.example.schedule.presentation.schedule.model.ScheduleDayModel
import java.time.LocalDate
import kotlin.math.min

@Composable
fun ScheduleDay(
    model: ScheduleDayModel,
    dndState: DragAndDropState,
    onAddGoalClick: () -> Unit,
    modifier: Modifier = Modifier,
    goalItem: @Composable (GoalItem) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val maxSizeDp = 500
    val width = remember(configuration) { min((configuration.screenWidthDp - 32), maxSizeDp).dp }
    val havePriorities by remember(model.priorities) {
        derivedStateOf { model.priorities.isNotEmpty() }
    }
    val haveAppointments by remember(model.appointments) {
        derivedStateOf { model.appointments.isNotEmpty() }
    }
    Box(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Surface(
            modifier = modifier.width(width),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScheduleDayHeader(
                    weekday = model.weekday,
                    date = model.dateText,
                    isToday = model.isToday,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
                if (!havePriorities && !haveAppointments) {
                    GoalItemPlaceholder(onClick = onAddGoalClick)
                } else {
                    if (havePriorities) {
                        GoalCategoryTitle(text = stringResource(id = R.string.priorities))
                        DropSurface { isInBound, _ ->
                            Column(
                                modifier = Modifier.dragAndDropBackground(
                                    isInBound,
                                    dndState.isDragging
                                ),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (item in model.priorities) {
                                    DragSurface(cardId = item.id) {
                                        goalItem(item)
                                    }
                                }
                                if (model.appointments.isNotEmpty())
                                    Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    if (haveAppointments) {
                        GoalCategoryTitle(text = stringResource(id = R.string.appointments))
                        DropSurface { isInBound, _ ->
                            Column(
                                modifier = Modifier.dragAndDropBackground(
                                    isInBound,
                                    dndState.isDragging
                                ),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (item in model.appointments) {
                                    DragSurface(cardId = item.id) {
                                        goalItem(item)
                                    }
                                }
                            }
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

fun Modifier.dragAndDropBackground(
    isInBound: Boolean,
    isDragging: Boolean
) = this.then(
    background(
        color = if (isInBound && isDragging) {
            Color(0xFF383838)
        } else {
            Color.Transparent
        }
    )
)

@Composable
private fun ScheduleDayHeader(
    weekday: String,
    date: String,
    isToday: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = weekday,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.alignByBaseline()
        )
        DotSeparator()
        Text(
            text = date,
            modifier = Modifier
                .alignByBaseline()
                .alpha(0.5f),
            style = MaterialTheme.typography.titleMedium
        )
        if (isToday) {
            DotSeparator()
            Text(
                text = stringResource(id = R.string.today),
                modifier = Modifier
                    .alignByBaseline()
                    .alpha(0.5f),
                style = MaterialTheme.typography.titleMedium
            )
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

@Composable
fun DotSeparator(
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = com.example.core.R.drawable.ic_circle),
        contentDescription = null,
        modifier = modifier
            .padding(top = 4.dp)
            .size(7.dp)
            .alpha(0.5f),
    )
}

@Preview
@Composable
fun ScheduleDayPreview() {
    DarkTheme {
        ScheduleDay(
            model = ScheduleDayModel(
                weekday = "Friday",
                date = LocalDate.now(),
                dateText = "Sep 25",
                isToday = true,
                priorities = listOf(
                    GoalItem(0, "Sample Goal", "Me"),
                    GoalItem(1, "Sample Goal", "Me")
                ),
                appointments = listOf(GoalItem(2, "Sample Goal", "Me")),
            ),
            dndState = DragAndDropState(),
            onAddGoalClick = {},
            goalItem = {
                GoalItem(
                    title = it.title,
                    role = it.role,
                    onClick = { },
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
            model = ScheduleDayModel(
                weekday = "Friday",
                date = LocalDate.now(),
                dateText = "Sep 25",
                isToday = true,
                priorities = emptyList(),
                appointments = emptyList(),
            ),
            dndState = DragAndDropState(),
            onAddGoalClick = {},
            goalItem = {
                GoalItem(
                    title = it.title,
                    role = it.role,
                    onClick = { },
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
            model = ScheduleDayModel(
                weekday = "Friday",
                date = LocalDate.now(),
                dateText = "Sep 25",
                isToday = true,
                priorities = listOf(
                    GoalItem(0, "Sample Goal", "Me"),
                    GoalItem(1, "Sample Goal", "Me")
                ),
                appointments = emptyList(),
            ),
            dndState = DragAndDropState(),
            onAddGoalClick = {},
            goalItem = {
                GoalItem(
                    title = it.title,
                    role = it.role,
                    onClick = { },
                    onCheck = { },
                )
            }
        )
    }
}