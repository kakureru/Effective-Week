package com.example.schedule.presentation.schedule.ui

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.R
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

@OptIn(ExperimentalMaterial3Api::class)
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
    val scaffoldState = rememberBottomSheetScaffoldState()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.toFloat()
    var expandedType by remember { mutableStateOf(ExpandedType.FULL) }
    val height by animateFloatAsState(
        when (expandedType) {
            ExpandedType.HALF -> screenHeight / 2
            ExpandedType.FULL -> screenHeight
            ExpandedType.COLLAPSED -> 56f
        }, label = ""
    )

    ScheduleScreenUi(
        dndState = dndState,
        scaffoldState = scaffoldState,
        state = state,
        effects = vm.uiEffect,
        modifier = modifier,
        topBar = { ScheduleTopBar() },
        sheetContent = {
            var isUpdated = false
            RolesTab(
                expanded = expandedType != ExpandedType.COLLAPSED,
                roles = state.roles,
                onAddRoleClick = { vm.accept(ScheduleEvent.AddRoleClick) },
                onEditClick = { roleName -> vm.accept(ScheduleEvent.EditRoleClick(roleName)) },
                onDeleteClick = { roleName -> vm.accept(ScheduleEvent.DeleteRoleClick(roleName)) },
                roleItem = { role ->
                    RoleItem(
                        dndState = dndState,
                        goals = role.goals.map { it.toGoalItem() },
                        onDropGoal = { goalId ->
                            vm.accept(
                                ScheduleEvent.GoalDropOnRole(
                                    goalId,
                                    role.name
                                )
                            )
                        },
                        onAddGoalClick = { vm.accept(ScheduleEvent.AddGoalToRoleClick(role.name)) },
                        goalItem = { goalItem -> goalItem(goalItem) }
                    )
                },
                modifier = Modifier.pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            if (!isUpdated) {
                                expandedType = when {
                                    dragAmount < 0 && expandedType == ExpandedType.COLLAPSED -> ExpandedType.HALF
                                    dragAmount < 0 && expandedType == ExpandedType.HALF -> ExpandedType.FULL
                                    dragAmount > 0 && expandedType == ExpandedType.FULL -> ExpandedType.HALF
                                    dragAmount > 0 && expandedType == ExpandedType.HALF -> ExpandedType.COLLAPSED
                                    else -> ExpandedType.FULL
                                }
                                isUpdated = true
                            }
                        },
                        onDragEnd = {
                            isUpdated = false
                        }
                    )
                }
            )
        },
        sheetPeekHeight = height.dp,
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

enum class ExpandedType {
    HALF, FULL, COLLAPSED
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreenUi(
    dndState: DragAndDropState,
    state: ScheduleState,
    scaffoldState: BottomSheetScaffoldState,
    effects: Flow<ScheduleEffect>,
    modifier: Modifier = Modifier,
    sheetPeekHeight: Dp,
    topBar: @Composable () -> Unit,
    sheetContent: @Composable ColumnScope.() -> Unit,
    scheduleDay: @Composable (ScheduleDayModel) -> Unit,
) {
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
        dndState = dndState
    ) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            modifier = modifier,
            topBar = topBar,
            sheetDragHandle = null,
            sheetPeekHeight = sheetPeekHeight,
            sheetContent = {
                DragListenSurface(
                    onEnter = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    },
                ) { isInBound ->
                    this@BottomSheetScaffold.sheetContent()
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            sheetShape = MaterialTheme.shapes.large.copy(
                bottomEnd = CornerSize(0.dp),
                bottomStart = CornerSize(0.dp)
            )
        ) { paddingValues ->
            LaunchedEffect(key1 = Unit) {
                scope.launch {
                    scaffoldState.bottomSheetState.partialExpand()
                }
            }
            val rowState = rememberLazyListState()
            val snapBehavior = rememberSnapFlingBehavior(lazyListState = rowState)
            val scrollAmount = 50f
            val dragListenerWidth = 70.dp
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
                    flingBehavior = snapBehavior,
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ScheduleScreenUiPreview() {
    DarkTheme {
        ScheduleScreenUi(
            dndState = DragAndDropState(),
            scaffoldState = rememberBottomSheetScaffoldState(),
            state = ScheduleState(),
            effects = emptyFlow(),
            sheetContent = {},
            topBar = { ScheduleTopBar() },
            scheduleDay = {},
            sheetPeekHeight = 56.dp
        )
    }
}