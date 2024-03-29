package com.effectiveweek.schedule.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.effectiveweek.core.ui.DraggableBottomSheet
import com.effectiveweek.core.ui.draganddrop.DragAndDropSurface
import com.effectiveweek.core.ui.draganddrop.DragListenSurface
import com.effectiveweek.schedule.presentation.roles_tab.RolesNavigation
import com.effectiveweek.schedule.presentation.roles_tab.ui.RolesTabAnchors
import com.effectiveweek.schedule.presentation.roles_tab.ui.RolesTabScreen
import com.effectiveweek.schedule.presentation.schedule.ScheduleNavigation
import com.effectiveweek.schedule.presentation.schedule.ui.ScheduleScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ScheduleWithRolesTabScreen(
    scheduleNavigation: ScheduleNavigation,
    rolesNavigation: RolesNavigation,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val sheetPeekHeightPx = with(density) { 50.dp.toPx() }
    val bottomSheetState = remember {
        AnchoredDraggableState(
            initialValue = RolesTabAnchors.COLLAPSED,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(150),
        )
    }

    DragAndDropSurface(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { layoutSize ->
                val dragEndPoint = layoutSize.height - sheetPeekHeightPx
                bottomSheetState.updateAnchors(
                    DraggableAnchors {
                        RolesTabAnchors.entries.forEach { it at dragEndPoint * it.fraction }
                    }
                )
            },
    ) {
        ScheduleScreen(navigation = scheduleNavigation)
        DraggableBottomSheet(
            bottomSheetState = bottomSheetState,
            content = {
                DragListenSurface(
                    onEnter = {
                        scope.launch {
                            bottomSheetState.animateTo(RolesTabAnchors.HALF)
                        }
                    },
                ) { _ ->
                    RolesTabScreen(navigation = rolesNavigation)
                }
            }
        )
    }
}