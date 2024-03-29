package com.effectiveweek.schedule.presentation.roles_tab.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.effectiveweek.core.ui.draganddrop.DragListenSurface
import com.effectiveweek.core.ui.draganddrop.LocalDragAndDropState
import com.effectiveweek.core.ui.theme.DarkTheme
import com.effectiveweek.schedule.R
import com.effectiveweek.schedule.domain.model.Goal
import com.effectiveweek.schedule.domain.model.Role
import com.effectiveweek.schedule.presentation.goal_item.GoalItem
import com.effectiveweek.schedule.presentation.goal_item.model.toGoalItem
import com.effectiveweek.schedule.presentation.roles_tab.RolesEffect
import com.effectiveweek.schedule.presentation.roles_tab.RolesEvent
import com.effectiveweek.schedule.presentation.roles_tab.RolesNavEvent
import com.effectiveweek.schedule.presentation.roles_tab.RolesNavigation
import com.effectiveweek.schedule.presentation.roles_tab.RolesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
internal fun RolesTabScreen(
    navigation: RolesNavigation,
    modifier: Modifier = Modifier,
    vm: RolesViewModel = koinViewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.navigationEvents.collect { event ->
            when (event) {
                is RolesNavEvent.OpenGoalDialogWithGoal -> navigation.openGoalDialog(event.goalId)
                is RolesNavEvent.OpenGoalDialogWithRole -> navigation.openGoalDialog(event.roleName)
                is RolesNavEvent.OpenRoleDialogWithRole -> navigation.openRoleDialog(event.roleName)
                RolesNavEvent.OpenRoleDialog -> navigation.openRoleDialog()
            }
        }
    }

    RolesTabUi(
        modifier = modifier,
        roles = state.roles,
        effects = vm.uiEffect,
        onAddRoleClick = { vm.accept(RolesEvent.AddRoleClick) },
        roleItem = { role ->
            RoleItem(
                isDragging = LocalDragAndDropState.current.isDragging,
                goals = role.goals.map { it.toGoalItem() },
                onDropGoal = { goalId ->
                    vm.accept(RolesEvent.GoalDropOnRole(goalId, role.name))
                },
                onAddGoalClick = { vm.accept(RolesEvent.AddGoalToRoleClick(role.name)) },
                onEditClick = { vm.accept(RolesEvent.EditRoleClick(role.name)) },
                onDeleteClick = { vm.accept(RolesEvent.DeleteRoleClick(role.name)) },
                name = role.name,
                goalItem = { goalItem ->
                    GoalItem(
                        title = goalItem.title,
                        role = goalItem.role,
                        onClick = { vm.accept(RolesEvent.GoalClick(goalItem.id)) },
                        onCheck = { vm.accept(RolesEvent.CompleteGoal(goalItem.id)) },
                    )
                }
            )
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RolesTabUi(
    roles: List<Role>,
    effects: Flow<RolesEffect>,
    onAddRoleClick: () -> Unit,
    modifier: Modifier = Modifier,
    roleItem: @Composable (Role) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val rolesRowState = rememberLazyListState()
    val scrollAmount = 50f
    val dragListenerWidth = 60.dp

    LaunchedEffect(Unit) {
        effects.collect {
            when (it) {
                is RolesEffect.Error -> scope.launch {
                    Toast.makeText(
                        context,
                        context.resources.getString(it.msgResource),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Surface(
        shape = MaterialTheme.shapes.large.copy(
            bottomEnd = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        ),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Box {
            DragListenSurface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(dragListenerWidth)
                    .align(Alignment.CenterStart),
                onAbove = {
                    rolesRowState.scrollBy(-scrollAmount)
                    delay(10)
                },
                zIndex = 2f
            )
            DragListenSurface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(dragListenerWidth)
                    .align(Alignment.CenterEnd),
                onAbove = {
                    rolesRowState.scrollBy(scrollAmount)
                    delay(10)
                },
                zIndex = 2f
            )
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                BottomSheetPeekHeader(height = 50.dp)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                    state = rolesRowState,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = rolesRowState), // FIXME crash on swipe on empty list
                    userScrollEnabled = LocalDragAndDropState.current.isDragging.not()
                ) {
                    items(items = roles, key = { item -> item.name }) {
                        roleItem(it)
                    }
                }
            }
            FilledIconButton(
                onClick = onAddRoleClick,
                modifier = Modifier
                    .padding(20.dp)
                    .size(52.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_role_add),
                    contentDescription = "add role",
                )
            }
        }
    }
}

@Composable
private fun BottomSheetPeekHeader(
    height: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height), contentAlignment = Alignment.TopCenter
    ) {
        DragHandle(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun DragHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp, 4.dp)
            .alpha(0.25f)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.onSurface)
    )
}

@Preview
@Composable
private fun RolesTabPreview() {
    DarkTheme {
        Surface(
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column {
                RolesTabUi(
                    roles = listOf(previewRole),
                    effects = emptyFlow(),
                    onAddRoleClick = {},
                    roleItem = {
                        RoleItem(
                            isDragging = false,
                            goals = previewRole.goals.map { it.toGoalItem() },
                            onDropGoal = { },
                            onAddGoalClick = { },
                            onEditClick = { },
                            onDeleteClick = { },
                            name = previewRole.name,
                            goalItem = { goalItem ->
                                GoalItem(
                                    title = goalItem.title,
                                    role = goalItem.role,
                                    onClick = { },
                                    onCheck = { },
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}

private val previewGoal = Goal(
    title = "Sample goal",
    role = "Sample role",
    description = "Sample description",
    appointment = false,
    time = LocalTime.now(),
    date = LocalDate.now(),
    id = 0
)

private val previewRole = Role(name = "Sample role", goals = listOf(previewGoal))
