package com.example.schedule.presentation.schedule.ui

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core.ui.DraggableBottomSheet
import com.example.core.ui.draganddrop.DragAndDropState
import com.example.core.ui.draganddrop.DragAndDropSurface
import com.example.core.ui.draganddrop.DragListenSurface
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.presentation.schedule.ScheduleEffect
import com.example.schedule.presentation.schedule.ScheduleEvent
import com.example.schedule.presentation.schedule.ScheduleNavEvent
import com.example.schedule.presentation.schedule.ScheduleNavigation
import com.example.schedule.presentation.schedule.ScheduleState
import com.example.schedule.presentation.schedule.ScheduleViewModel
import com.example.schedule.presentation.schedule.model.GoalItem
import com.example.schedule.presentation.schedule.model.ScheduleDayModel
import com.example.schedule.presentation.schedule.model.toGoalItem
import kotlinx.coroutines.delay
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

    LaunchedEffect(Unit) {
        vm.navigationEvents.collect { event ->
            when (event) {
                is ScheduleNavEvent.OpenGoalDialogWithDate -> navigation.openGoalDialog(event.epochDay)
                is ScheduleNavEvent.OpenGoalDialogWithGoal -> navigation.openGoalDialog(event.goalId)
                is ScheduleNavEvent.OpenGoalDialogWithRole -> navigation.openGoalDialog(event.roleName)
                is ScheduleNavEvent.OpenRoleDialogWithRole -> navigation.openRoleDialog(event.roleName)
                ScheduleNavEvent.OpenRoleDialog -> navigation.openRoleDialog()
            }
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
        bottomSheetContent = {
            RolesTab(
                roles = state.roles,
                onAddRoleClick = { vm.accept(ScheduleEvent.AddRoleClick) },
                onEditClick = { roleName -> vm.accept(ScheduleEvent.EditRoleClick(roleName)) },
                onDeleteClick = { roleName -> vm.accept(ScheduleEvent.DeleteRoleClick(roleName)) },
                roleItem = { role ->
                    RoleItem(
                        isDragging = dndState.isDragging,
                        goals = role.goals.map { it.toGoalItem() },
                        onDropGoal = { goalId ->
                            vm.accept(ScheduleEvent.GoalDropOnRole(goalId, role.name))
                        },
                        onAddGoalClick = { vm.accept(ScheduleEvent.AddGoalToRoleClick(role.name)) },
                        goalItem = { goalItem -> goalItem(goalItem) }
                    )
                },
            )
        },
        scheduleDay = {
            ScheduleDay(
                isDragging = dndState.isDragging,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreenUi(
    dndState: DragAndDropState,
    state: ScheduleState,
    effects: Flow<ScheduleEffect>,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    bottomSheetContent: @Composable () -> Unit,
    scheduleDay: @Composable (ScheduleDayModel) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val rowState = rememberLazyListState()
    val scrollAmount = 50f
    val dragListenerWidth = 70.dp

    val density = LocalDensity.current
    val sheetPeekHeightPx = with(density) { 50.dp.toPx() }
    val sheetState = remember {
        AnchoredDraggableState(
            initialValue = RolesTabAnchors.COLLAPSED,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(150),
        )
    }

    LaunchedEffect(Unit) {
        effects.collect {
            when (it) {
                is ScheduleEffect.Error -> scope.launch {
                    Toast.makeText(context, context.resources.getString(it.msgResource), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    DragAndDropSurface(
        modifier = Modifier.fillMaxSize(),
        dndState = dndState
    ) {
        Scaffold(
            modifier = modifier
                .onSizeChanged { layoutSize ->
                    val dragEndPoint = layoutSize.height - sheetPeekHeightPx
                    sheetState.updateAnchors(
                        DraggableAnchors {
                            RolesTabAnchors.entries.forEach { it at dragEndPoint * it.fraction }
                        }
                    )
                },
            topBar = topBar,
            containerColor = MaterialTheme.colorScheme.background,
        ) { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                DragListenSurface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(dragListenerWidth)
                        .align(Alignment.CenterStart),
                ) { isInBound ->
                    LaunchedEffect(isInBound) {
                        while (isInBound) {
                            rowState.scrollBy(-scrollAmount)
                            delay(10)
                        }
                    }
                }
                DragListenSurface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(dragListenerWidth)
                        .align(Alignment.CenterEnd),
                ) { isInBound ->
                    LaunchedEffect(isInBound) {
                        while (isInBound) {
                            rowState.scrollBy(scrollAmount)
                            delay(10)
                        }
                    }
                }
                LazyRow(
                    state = rowState,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = rowState),
                    contentPadding = PaddingValues(horizontal = 8.dp),
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
        DraggableBottomSheet(
            bottomSheetState = sheetState,
            content = {
                DragListenSurface(
                    onEnter = {
                        scope.launch {
                            sheetState.animateTo(RolesTabAnchors.HALF)
                        }
                    },
                ) { _ ->
                    bottomSheetContent()
                }
            }
        )
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
            bottomSheetContent = {},
            topBar = { ScheduleTopBar() },
            scheduleDay = {},
        )
    }
}