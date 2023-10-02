package com.example.schedule.presentation.schedule.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.DarkTheme
import com.example.core.R
import com.example.schedule.domain.model.Role
import com.example.schedule.presentation.schedule.ScheduleState
import com.example.schedule.presentation.schedule.ScheduleViewModel
import com.example.schedule.presentation.schedule.model.GoalCallback
import java.time.LocalDate

@Composable
fun ScheduleScreen(
    goalCallback: GoalCallback,
    onAddGoalToScheduleClick: (epochDay: Long) -> Unit,
    onAddGoalToRoleClick: (roleName: String) -> Unit,
    onDeleteRoleClick: (roleName: String) -> Unit,
    onEditRoleClick: (roleName: String) -> Unit,
    onAddRoleClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel,
) {
    val state by viewModel.uiState.collectAsState()
    ScheduleScreenUi(
        state = state,
        onAddGoalToScheduleClick = onAddGoalToScheduleClick,
        goalCallback = goalCallback,
        modifier = modifier,
        topBar = {
            ScheduleTopBar()
        },
        sheetContent = {
            RolesTab(
                roles = state.roles,
                goalCallback = goalCallback,
                onAddRoleClick = onAddRoleClick,
                onDeleteClick = onDeleteRoleClick,
                onAddGoalClick = onAddGoalToRoleClick,
                onEditClick = onEditRoleClick
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreenUi(
    state: ScheduleState,
    onAddGoalToScheduleClick: (epochDay: Long) -> Unit,
    goalCallback: GoalCallback,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    sheetContent: @Composable ColumnScope.() -> Unit,
) {
    BottomSheetScaffold(
        modifier = modifier,
        topBar = topBar,
        sheetContent = sheetContent,
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        val rowState = rememberLazyListState()
        val snapBehavior = rememberSnapFlingBehavior(lazyListState = rowState)
        LazyRow(
            state = rowState,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            flingBehavior = snapBehavior,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 16.dp)
                .fillMaxHeight(),
        ) {
            items(items = state.schedule, key = { item -> item.dateText }) {
                ScheduleDay(
                    weekday = it.weekday,
                    date = it.dateText,
                    isToday = it.isToday,
                    priorities = it.priorities,
                    appointments = it.appointments,
                    onAddGoalClick = { onAddGoalToScheduleClick(it.date.toEpochDay()) },
                    goalItem = { goal ->
                        GoalItem(
                            title = goal.title,
                            role = goal.role,
                            onClick = { goalCallback.onClick(goal.id) },
                            onLongClick = { },
                            onCheck = { goalCallback.onCompleteClick(goal.id) },
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleTopBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier
    )
}

@Preview
@Composable
fun ScheduleScreenUiPreview() {
    DarkTheme {
        ScheduleScreenUi(
            state = ScheduleState(),
            onAddGoalToScheduleClick = {},
            goalCallback = previewGoalCallback,
            sheetContent = {},
            topBar = { ScheduleTopBar() }
        )
    }
}