package com.effectiveweek.schedule.presentation.schedule.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import com.effectiveweek.core.ui.AddButton
import com.effectiveweek.core.ui.draganddrop.DragData
import com.effectiveweek.core.ui.draganddrop.DragListenSurface
import com.effectiveweek.core.ui.draganddrop.DragSurface
import com.effectiveweek.core.ui.draganddrop.LocalDragAndDropState
import com.effectiveweek.core.ui.theme.DarkTheme
import com.effectiveweek.schedule.R
import com.effectiveweek.schedule.presentation.goal_item.GoalItem
import com.effectiveweek.schedule.presentation.goal_item.model.GoalItem
import com.effectiveweek.schedule.presentation.schedule.model.ScheduleDayModel
import java.time.LocalDate
import kotlin.math.min

@Composable
internal fun ScheduleDay(
    modelProvider: () -> ScheduleDayModel,
    onAddGoalClick: () -> Unit,
    onDropGoalToPriorities: (goalId: Int) -> Unit,
    onDropGoalToAppointments: (goalId: Int) -> Unit,
    modifier: Modifier = Modifier,
    goalItem: @Composable (GoalItem) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val maxSizeDp = 500
    val width = remember(configuration) { min((configuration.screenWidthDp), maxSizeDp).dp }
    val havePriorities by remember(modelProvider().priorities) {
        derivedStateOf { modelProvider().priorities.isNotEmpty() }
    }
    val haveAppointments by remember(modelProvider().appointments) {
        derivedStateOf { modelProvider().appointments.isNotEmpty() }
    }
    LazyColumn(
        modifier = modifier
            .padding(bottom = 16.dp)
            .width(width)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            ScheduleDayHeader(
                weekday = modelProvider().weekday,
                date = modelProvider().dateNumber,
                isToday = modelProvider().isToday,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 4.dp),
                onAddGoalClick = onAddGoalClick
            )
        }
        if (havePriorities) {
            item {
                GoalCategoryTitle(
                    text = stringResource(id = R.string.priorities),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                DragListenSurface(
                    zIndex = 1f,
                    onDrop = { dragData -> onDropGoalToPriorities(dragData.id) }
                ) { isInBound ->
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .dragAndDropBackground(
                                isInBound,
                                LocalDragAndDropState.current.isDragging
                            ),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (item in modelProvider().priorities) {
                            key(item.id) {
                                DragSurface(
                                    cardId = item.id,
                                    dragData = object : DragData {
                                        override val id: Int = item.id
                                    }
                                ) {
                                    goalItem(item)
                                }
                            }
                        }
                        if (modelProvider().appointments.isNotEmpty())
                            Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        if (haveAppointments) {
            item {
                GoalCategoryTitle(
                    text = stringResource(id = R.string.appointments),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                DragListenSurface(
                    zIndex = 1f,
                    onDrop = { dragData -> onDropGoalToAppointments(dragData.id) }
                ) { isInBound ->
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .dragAndDropBackground(
                                isInBound,
                                LocalDragAndDropState.current.isDragging
                            ),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (item in modelProvider().appointments) {
                            key(item.id) {
                                DragSurface(
                                    cardId = item.id,
                                    dragData = object : DragData {
                                        override val id: Int = item.id
                                    }
                                ) {
                                    goalItem(item)
                                }
                            }
                        }
                    }
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
    onAddGoalClick: () -> Unit,
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
        )
        DotSeparator()
        Text(
            text = date,
            modifier = Modifier
                .alpha(0.5f),
            style = MaterialTheme.typography.titleMedium
        )
        if (isToday) {
            DotSeparator()
            Text(
                text = stringResource(id = R.string.today),
                modifier = Modifier
                    .alpha(0.5f),
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        AddButton(onClick = onAddGoalClick)
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
//            .padding(bottom = 8.dp)
            .alpha(0.5f)
    )
}

@Composable
private fun DotSeparator(
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = com.effectiveweek.core.R.drawable.ic_circle),
        contentDescription = null,
        modifier = modifier
            .size(7.dp)
            .alpha(0.5f),
    )
}

@Preview
@Composable
private fun ScheduleDayPreview() {
    DarkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ScheduleDay(
                modelProvider = {
                    ScheduleDayModel(
                        weekday = "Friday",
                        date = LocalDate.now(),
                        dateNumber = "25",
                        isToday = true,
                        priorities = listOf(
                            GoalItem(0, "Sample Goal", "Me"),
                            GoalItem(1, "Sample Goal", "Me")
                        ),
                        appointments = listOf(GoalItem(2, "Sample Goal", "Me")),
                    )
                },
                onAddGoalClick = {},
                onDropGoalToPriorities = {},
                onDropGoalToAppointments = {},
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
}

@Preview(backgroundColor = 0xFF000000)
@Composable
private fun ScheduleDayPreviewNoAppointments() {
    DarkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ScheduleDay(
                modelProvider = {
                    ScheduleDayModel(
                        weekday = "Friday",
                        date = LocalDate.now(),
                        dateNumber = "25",
                        isToday = true,
                        priorities = listOf(
                            GoalItem(0, "Sample Goal", "Me"),
                            GoalItem(1, "Sample Goal", "Me")
                        ),
                        appointments = emptyList(),
                    )
                },
                onAddGoalClick = {},
                onDropGoalToPriorities = {},
                onDropGoalToAppointments = {},
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
}