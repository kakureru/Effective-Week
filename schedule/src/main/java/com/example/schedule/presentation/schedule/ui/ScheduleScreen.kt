package com.example.schedule.presentation.schedule.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core.ui.draganddrop.DragAndDropState
import com.example.core.ui.draganddrop.DragAndDropSurface
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.presentation.schedule.ScheduleEffect
import com.example.schedule.presentation.schedule.ScheduleEvent
import com.example.schedule.presentation.schedule.ScheduleNavState
import com.example.schedule.presentation.schedule.ScheduleNavigation
import com.example.schedule.presentation.schedule.ScheduleState
import com.example.schedule.presentation.schedule.ScheduleViewModel
import com.example.schedule.presentation.schedule.model.GoalItem
import com.example.schedule.presentation.schedule.model.ScheduleDayModel
import com.example.schedule.presentation.schedule.model.toGoalItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun ScheduleScreen(
    navigation: ScheduleNavigation,
    vm: ScheduleViewModel,
    modifier: Modifier = Modifier,
) {
    val dndState = remember { DragAndDropState() }
    val state by vm.uiState.collectAsState()

    LaunchedEffect(state.navState) {
        when (val navState = state.navState) {
            ScheduleNavState.Idle -> Unit
            is ScheduleNavState.OpenGoalDialogWithDate -> navigation.openGoalDialog(navState.epochDay)
            is ScheduleNavState.OpenGoalDialogWithGoal -> navigation.openGoalDialog(navState.goalId)
            is ScheduleNavState.OpenGoalDialogWithRole -> navigation.openGoalDialog(navState.roleName)
            is ScheduleNavState.OpenRoleDialogWithRole -> navigation.openRoleDialog(navState.roleName)
            ScheduleNavState.OpenRoleDialog -> navigation.openRoleDialog()
        }
    }

    val goalItem: @Composable (GoalItem) -> Unit = { goalItem ->
        GoalItem(
            title = goalItem.title,
            role = goalItem.role,
            onClick = { vm.accept(ScheduleEvent.GoalClick(goalItem.id)) },
            onCheck = { vm.accept(ScheduleEvent.CompleteGoal(goalItem.id)) },
        )
    }

    ScheduleScreenUi(
        dndState = dndState,
        state = state,
        effects = vm.uiEffect,
        modifier = modifier,
        topBar = { ScheduleTopBar() },
        sheetContent = {
            RolesTab(
                roles = state.roles,
                onAddRoleClick = { vm.accept(ScheduleEvent.AddRoleClick) },
                roleItem = { role ->
                    RoleItem(
                        dndState = dndState,
                        name = role.name,
                        goals = role.goals.map { it.toGoalItem() },
                        onAddGoalClick = { vm.accept(ScheduleEvent.AddGoalToRoleClick(role.name)) },
                        onEditClick = { vm.accept(ScheduleEvent.EditRoleClick(role.name)) },
                        onDeleteClick = { vm.accept(ScheduleEvent.DeleteRoleClick(role.name)) },
                        goalItem = { goalItem -> goalItem(goalItem) }
                    )
                }
            )
        },
        scheduleDay = {
            ScheduleDay(
                dndState = dndState,
                model = it,
                onAddGoalClick = { vm.accept(ScheduleEvent.AddGoalToScheduleDayClick(it.date.toEpochDay())) },
                goalItem = { goalItem -> goalItem(goalItem) },
                modifier = Modifier.padding(vertical = 16.dp),
                onDropGoalToPriorities = { goalId ->
                    vm.accept(
                        ScheduleEvent.GoalDropOnSchedule(
                            goalId = goalId,
                            date = it.date,
                            isAppointment = false
                        )
                    )
                },
                onDropGoalToAppointments = { goalId ->
                    vm.accept(
                        ScheduleEvent.GoalDropOnSchedule(
                            goalId = goalId,
                            date = it.date,
                            isAppointment = true,
                        )
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreenUi(
    dndState: DragAndDropState,
    state: ScheduleState,
    effects: Flow<ScheduleEffect>,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    sheetContent: @Composable ColumnScope.() -> Unit,
    scheduleDay: @Composable (ScheduleDayModel) -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        effects.collect {
            when (it) {
                is ScheduleEffect.Error -> scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(context.resources.getString(it.msgResource))
                }
            }
        }
    }

    DragAndDropSurface(
        modifier = Modifier.fillMaxSize(),
        state = dndState
    ) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            modifier = modifier,
            topBar = topBar,
            sheetContent = sheetContent,
            containerColor = MaterialTheme.colorScheme.background,
            sheetShape = MaterialTheme.shapes.large
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
                    .fillMaxHeight(),
            ) {
                items(items = state.schedule, key = { item -> item.dateText }) {
                    scheduleDay(it)
                }
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
            dndState = DragAndDropState(),
            state = ScheduleState(),
            effects = emptyFlow(),
            sheetContent = {},
            topBar = { ScheduleTopBar() },
            scheduleDay = {}
        )
    }
}