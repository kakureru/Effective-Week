package com.effectiveweek.core.ui.draganddrop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.delay

@Composable
fun DragListenSurface(
    modifier: Modifier = Modifier,
    onDrop: ((DragData) -> Unit)? = null,
    onEnter: (() -> Unit)? = null,
    onExit: (() -> Unit)? = null,
    onAbove: (suspend () -> Unit)? = null,
    zIndex: Float = 1f,
    content: @Composable BoxScope.(isInBound: Boolean) -> Unit = { Unit }
) {
    val dndState = LocalDragAndDropState.current
    val dragOffset = dndState.dragPosition
    var isAboveDropSurface by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(isAboveDropSurface) {
        if (isAboveDropSurface) {
            onDrop?.let { dndState.onDropZoneEnter(it, zIndex) }
            onEnter?.invoke()
        }
        else {
            onDrop?.let { dndState.onDropZoneLeave(it, zIndex) }
            onAbove?.let { dndState.onAboveZoneLeave(zIndex) }
            onExit?.invoke()
        }
        while (isAboveDropSurface) {
            onAbove?.let { dndState.onAboveDropZone(it, zIndex) }
            delay(1) // to avoid ui lock
        }
    }
    Box(
        modifier = modifier.onGloballyPositioned {
            it.boundsInWindow().let { rect ->
                isAboveDropSurface = dndState.isDragging && rect.contains(dragOffset)
            }
        }
    ) {
        content(isAboveDropSurface)
    }
}