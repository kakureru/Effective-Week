package com.example.schedule.presentation.schedule.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core.ui.AddButton
import com.example.core.ui.draganddrop.DragAndDropState
import com.example.core.ui.draganddrop.DragData
import com.example.core.ui.draganddrop.DragListenSurface
import com.example.core.ui.draganddrop.DragSurface
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.presentation.schedule.model.GoalItem
import kotlin.math.min

@Composable
fun RoleItem(
    dndState: DragAndDropState,
    goals: List<GoalItem>,
    onDropGoal: (goalId: Int) -> Unit,
    onAddGoalClick: () -> Unit,
    modifier: Modifier = Modifier,
    goalItem: @Composable (GoalItem) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val maxSizeDp = 500
    val width = remember(configuration) { min((configuration.screenWidthDp - 16), maxSizeDp).dp }
    val haveGoals by remember(goals) { derivedStateOf { goals.isNotEmpty() } }

    DragListenSurface(
        onDrop = { dropData -> onDropGoal(dropData.id) },
        zIndex = 2f
    ) { isInBound ->
        Box(
            modifier = modifier.width(width),
        ) {
            LazyColumn(
                modifier = Modifier
                    .dragAndDropBackground(isInBound, dndState.isDragging)
                    .animateContentSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (haveGoals) {
                    items(items = goals, key = { item -> item.id }) { item ->
                        DragSurface(
                            cardId = item.id,
                            dragData = object : DragData {
                                override val id: Int = item.id
                            }
                        ) {
                            goalItem(item)
                        }
                    }
                    item {
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AddButton(
                                onClick = onAddGoalClick,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                    }
                } else {
                    item {
                        GoalItemPlaceholder(
                            onClick = onAddGoalClick,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RoleOptionsDropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.action_edit)) },
            onClick = {
                onEditClick()
                onDismiss()
            }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.action_delete)) },
            onClick = {
                onDeleteClick()
                onDismiss()
            }
        )
    }

}

@Preview
@Composable
fun RoleItemPreview() {
    DarkTheme {
        RoleItem(
            dndState = DragAndDropState(),
            onAddGoalClick = {},
            goals = listOf(GoalItem(0, "Sample Goal", "Me"), GoalItem(1, "Sample Goal", "Me")),
            onDropGoal = {},
            goalItem = {
                GoalItem(
                    title = it.title,
                    role = it.role,
                    onClick = { },
                    onCheck = { }
                )
            }
        )
    }
}

@Preview
@Composable
fun RoleItemPreviewNoGoals() {
    DarkTheme {
        RoleItem(
            dndState = DragAndDropState(),
            onAddGoalClick = {},
            goals = emptyList(),
            onDropGoal = {},
            goalItem = {
                GoalItem(
                    title = it.title,
                    role = it.role,
                    onClick = { },
                    onCheck = { }
                )
            }
        )
    }
}