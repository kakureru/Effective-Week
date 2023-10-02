package com.example.schedule.presentation.schedule.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.core.ui.AddButton
import com.example.core.ui.theme.DarkTheme
import com.example.core.R
import com.example.schedule.presentation.schedule.model.GoalCallback
import com.example.schedule.presentation.schedule.model.GoalItem
import kotlin.math.min

@Composable
fun RoleItem(
    name: String,
    goals: List<GoalItem>,
    goalCallback: GoalCallback,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddGoalClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val maxSizeDp = 500
    val width = remember(configuration) { min((configuration.screenWidthDp - 32), maxSizeDp).dp }
    var optionsExpanded by remember { mutableStateOf(false) }
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.width(width),
        tonalElevation = 2.dp
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = name)
                    Spacer(modifier = Modifier.weight(1f))
                    Box {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "more",
                            modifier = Modifier.clickable { optionsExpanded = true }
                        )
                        RoleOptionsDropdownMenu(
                            expanded = optionsExpanded,
                            onDismiss = { optionsExpanded = false },
                            onEditClick = onEditClick,
                            onDeleteClick = onDeleteClick
                        )
                    }
                }
            }
            items(items = goals, key = { item -> item.id }) {
                GoalItem(
                    title = it.title,
                    role = it.role,
                    onClick = { goalCallback.onClick(it.id) },
                    onLongClick = { /*TODO*/ },
                    onCheck = { goalCallback.onCompleteClick(it.id) })
            }
            item {
                AddButton(onClick = onAddGoalClick)
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
            name = "Sample role",
            onAddGoalClick = {},
            goals = emptyList(),
            goalCallback = previewGoalCallback,
            onDeleteClick = {},
            onEditClick = {},
        )
    }
}

val previewGoalCallback = object : GoalCallback {
    override fun onCompleteClick(goalId: Int) = Unit
    override fun onClick(goalId: Int) = Unit
}