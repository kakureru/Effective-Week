package com.example.schedule.presentation.schedule.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.R
import com.example.schedule.domain.model.Role
import com.example.schedule.presentation.schedule.model.GoalCallback
import com.example.schedule.presentation.schedule.model.toGoalItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.RolesTab(
    roles: List<Role>,
    goalCallback: GoalCallback,
    onAddRoleClick: () -> Unit,
    onDeleteClick: (roleName: String) -> Unit,
    onAddGoalClick: (roleName: String) -> Unit,
    onEditClick: (roleName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val rowState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = rowState)
    Column(modifier = Modifier.height((configuration.screenHeightDp / 2).dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(text = stringResource(id = R.string.my_roles))
            Icon(
                painter = painterResource(id = R.drawable.ic_role_add),
                contentDescription = "add role",
                modifier = Modifier.clickable { onAddRoleClick() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
            state = rowState,
            flingBehavior = snapBehavior,
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(items = roles, key = { item -> item.name }) {
                RoleItem(
                    name = it.name,
                    goals = it.goals.map { it.toGoalItem() },
                    goalCallback = goalCallback,
                    onAddGoalClick = { onAddGoalClick(it.name) },
                    onEditClick = { onEditClick(it.name) },
                    onDeleteClick = { onDeleteClick(it.name) },
                    goalItem = { goalItem ->
                        GoalItem(
                            title = goalItem.title,
                            role = goalItem.role,
                            onClick = { goalCallback.onClick(goalItem.id) },
                            onLongClick = { /*TODO*/ },
                            onCheck = { goalCallback.onCompleteClick(goalItem.id) }
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun RolesTabPreview() {
    DarkTheme {
        Surface(
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column {
                RolesTab(
                    modifier = Modifier.padding(top = 16.dp),
                    roles = emptyList(),
                    goalCallback = previewGoalCallback,
                    onDeleteClick = {},
                    onAddGoalClick = {},
                    onEditClick = {},
                    onAddRoleClick = {},
                )
            }
        }
    }
}