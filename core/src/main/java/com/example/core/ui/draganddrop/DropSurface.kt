package com.example.core.ui.draganddrop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun DropSurface(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(isInBound: Boolean, dragOffset: Offset) -> Unit
) {
    val dragNDropState = LocalDragAndDropState.current
    val dragPosition = dragNDropState.itemPosition
    val dragOffset = dragNDropState.dragOffset
    var isCurrentDropTarget by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = modifier.onGloballyPositioned {
            it.boundsInWindow().let { rect ->
                isCurrentDropTarget = rect.contains(dragPosition + dragOffset)

            }
        }
    ) {
        content(isCurrentDropTarget, dragOffset)
    }
}