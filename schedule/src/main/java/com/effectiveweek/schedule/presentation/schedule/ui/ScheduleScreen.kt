package com.effectiveweek.schedule.presentation.schedule.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.effectiveweek.core.ui.draganddrop.DragListenSurface
import com.effectiveweek.core.ui.theme.DarkTheme
import com.effectiveweek.schedule.R
import com.effectiveweek.schedule.presentation.schedule.ScheduleEffect
import com.effectiveweek.schedule.presentation.schedule.ScheduleEvent
import com.effectiveweek.schedule.presentation.schedule.ScheduleUiState
import com.effectiveweek.schedule.presentation.schedule.ScheduleViewModel
import com.effectiveweek.schedule.presentation.schedule.model.ScheduleDayModel
import com.effectiveweek.schedule.presentation.GoalItem
import com.effectiveweek.schedule.presentation.schedule.ScheduleNavEvent
import com.effectiveweek.schedule.presentation.schedule.ScheduleNavigation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScheduleScreen(
    navigation: ScheduleNavigation,
    modifier: Modifier = Modifier,
    vm: ScheduleViewModel = koinViewModel(),
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.navigationEvents.collect { event ->
            when (event) {
                is ScheduleNavEvent.OpenGoalDialogWithDate -> navigation.openGoalDialog(event.epochDay)
                is ScheduleNavEvent.OpenGoalDialogWithGoal -> navigation.openGoalDialog(event.goalId)
            }
        }
    }

    ScheduleScreenUi(
        stateProvider = { state },
        effects = vm.uiEffect,
        modifier = modifier,
        topBar = {
            ScheduleTopBar(
                month = { state.month },
                onTodayClick = { vm.accept(ScheduleEvent.TodayClick) }
            )
        },
        onFirstVisibleDayIndexChange = { index ->
            vm.accept(
                ScheduleEvent.FirstVisibleDayIndexChange(
                    index
                )
            )
        },
        onLastVisibleDayIndexChange = { index ->
            vm.accept(
                ScheduleEvent.LastVisibleDayIndexChange(
                    index
                )
            )
        },
        scheduleDay = {
            ScheduleDay(
                modelProvider = { it },
                onAddGoalClick = { vm.accept(ScheduleEvent.AddGoalToScheduleDayClick(it.date.toEpochDay())) },
                goalItem = { goalItem ->
                    GoalItem(
                        title = goalItem.title,
                        role = goalItem.role,
                        onClick = { vm.accept(ScheduleEvent.GoalClick(goalItem.id)) },
                        onCheck = { vm.accept(ScheduleEvent.CompleteGoal(goalItem.id)) },
                    )
                },
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
    stateProvider: () -> ScheduleUiState,
    effects: Flow<ScheduleEffect>,
    onFirstVisibleDayIndexChange: (index: Int) -> Unit,
    onLastVisibleDayIndexChange: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    scheduleDay: @Composable (ScheduleDayModel) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scheduleRowState = rememberLazyListState()
    val scrollAmount = 50f
    val dragListenerWidth = 70.dp

    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when (effect) {
                is ScheduleEffect.Error -> scope.launch {
                    Toast.makeText(
                        context,
                        context.resources.getString(effect.msgResource),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ScheduleEffect.ScrollToDay -> scope.launch {
                    scheduleRowState.animateScrollToItem(effect.index)
                }
            }
        }
    }

    LaunchedEffect(scheduleRowState) {
        snapshotFlow {
            scheduleRowState.layoutInfo.visibleItemsInfo.firstOrNull()?.index
        }.collectLatest {
            it?.let { onFirstVisibleDayIndexChange(it) }
        }
    }

    LaunchedEffect(scheduleRowState) {
        snapshotFlow {
            scheduleRowState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collectLatest {
            it?.let { onLastVisibleDayIndexChange(it) }
        }
    }

    Scaffold(
        modifier = modifier,
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
                        scheduleRowState.scrollBy(-scrollAmount)
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
                        scheduleRowState.scrollBy(scrollAmount)
                        delay(10)
                    }
                }
            }
            LazyRow(
                state = scheduleRowState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                flingBehavior = rememberSnapFlingBehavior(lazyListState = scheduleRowState),
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxHeight(),
            ) {
                Log.d("MYTAG", "LazyRow composed")
                items(items = stateProvider().schedule, key = { item -> item.dateNumber }) {
                    scheduleDay(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleTopBar(
    month: () -> String,
    onTodayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = month(), modifier = Modifier.alpha(0.5f)) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier,
        actions = {
            IconButton(onClick = onTodayClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_today),
                    contentDescription = null
                )
            }
        }
    )
}

@Preview
@Composable
fun ScheduleScreenUiPreview() {
    DarkTheme {
        ScheduleScreenUi(
            stateProvider = { ScheduleUiState() },
            effects = emptyFlow(),
            topBar = { ScheduleTopBar(onTodayClick = {}, month = { "November" }) },
            scheduleDay = {},
            onFirstVisibleDayIndexChange = {},
            onLastVisibleDayIndexChange = {}
        )
    }
}