package com.example.schedule.presentation.schedule.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.R
import com.example.schedule.domain.model.Role
import kotlin.math.abs

enum class ExpandedType {
    HALF, FULL, COLLAPSED
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.RolesTab(
    roles: List<Role>,
    onAddRoleClick: () -> Unit,
    onEditClick: (roleName: String) -> Unit,
    onDeleteClick: (roleName: String) -> Unit,
    modifier: Modifier = Modifier,
    roleItem: @Composable (Role) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val rowState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = rowState)
    val name by remember {
        derivedStateOf {
            rowState.layoutInfo.visibleItemsInfo.minByOrNull { abs(it.offset) }?.key.toString()
        }
    }

    var expandedType by remember {
        mutableStateOf(ExpandedType.HALF)
    }
    val height by remember(expandedType) {
       derivedStateOf {
           when (expandedType) {
               ExpandedType.HALF -> configuration.screenHeightDp / 2
               ExpandedType.FULL -> configuration.screenHeightDp
               ExpandedType.COLLAPSED -> 50
           }
       }
    }
    var isUpdated = false

    Box(modifier = Modifier.animateContentSize(animationSpec = tween(200))) {
        Column(
            modifier = modifier
                .height(height.dp)
                .fillMaxWidth()
                .pointerInput(Unit) {
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
        ) {
            DragHandle(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp, bottom = 16.dp)
            )
            RolesTabHeader(
                title = name,
                onAddRoleClick = onAddRoleClick,
                onDeleteClick = { onDeleteClick(name) },
                onEditClick = { onEditClick(name) },
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
                state = rowState,
                flingBehavior = snapBehavior,
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                items(items = roles, key = { item -> item.name }) {
                    roleItem(it)
                }
            }
        }
    }
}

@Composable
fun RolesTabHeader(
    title: String,
    modifier: Modifier = Modifier,
    onAddRoleClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    var optionsExpanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_role_add),
            contentDescription = "add role",
            modifier = Modifier
                .clickable { onAddRoleClick() }
                .padding(end = 8.dp)
        )
        Box(
            modifier = Modifier
        ) {
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
                RolesTab(
                    roles = emptyList(),
                    onAddRoleClick = {},
                    roleItem = {},
                    onEditClick = {},
                    onDeleteClick = {}
                )
            }
        }
    }
}