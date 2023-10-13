package com.effectiveweek.core.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> DraggableBottomSheet(
    bottomSheetState: AnchoredDraggableState<T>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .offset {
                IntOffset(x = 0, y = bottomSheetState.requireOffset().roundToInt())
            }
            .anchoredDraggable(bottomSheetState, Orientation.Vertical),
    ) {
        content()
    }
}