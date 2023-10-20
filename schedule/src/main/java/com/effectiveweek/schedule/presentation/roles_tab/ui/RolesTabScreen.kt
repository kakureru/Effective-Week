package com.effectiveweek.schedule.presentation.roles_tab.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.effectiveweek.core.ui.theme.DarkTheme
import com.effectiveweek.schedule.R
import com.effectiveweek.schedule.domain.model.Goal
import com.effectiveweek.schedule.domain.model.Role
import com.effectiveweek.schedule.presentation.GoalItem
import com.effectiveweek.schedule.presentation.model.toGoalItem
import com.effectiveweek.schedule.presentation.roles_tab.RolesEffect
import com.effectiveweek.schedule.presentation.roles_tab.RolesEvent
import com.effectiveweek.schedule.presentation.roles_tab.RolesNavEvent
import com.effectiveweek.schedule.presentation.roles_tab.RolesNavigation
import com.effectiveweek.schedule.presentation.roles_tab.RolesViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun RolesTabScreen(
    navigation: RolesNavigation,
    isDragging: Boolean,
    vm: RolesViewModel = koinViewModel(),
) {
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        vm.uiEffect.collect {
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
        roles = state.roles,
        onAddRoleClick = { vm.accept(RolesEvent.AddRoleClick) },
        roleItem = { role ->
            RoleItem(
                isDragging = isDragging,
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
fun RolesTabUi(
    roles: List<Role>,
    onAddRoleClick: () -> Unit,
    modifier: Modifier = Modifier,
    roleItem: @Composable (Role) -> Unit,
) {
    val rowState = rememberLazyListState()

    Surface(
        shape = MaterialTheme.shapes.large.copy(
            bottomEnd = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        )
    ) {
        Box {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                BottomSheetPeekHeader(height = 50.dp)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                    state = rowState,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = rowState), // FIXME crash on swipe on empty list
                    contentPadding = PaddingValues(horizontal = 8.dp),
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
                    .size(48.dp)
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
fun BottomSheetPeekHeader(
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
fun DragHandle(modifier: Modifier = Modifier) {
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
fun RolesTabPreview() {
    DarkTheme {
        Surface(
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column {
                RolesTabUi(
                    roles = listOf(previewRole),
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
