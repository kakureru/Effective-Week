package com.example.core.ui.draganddrop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    onDrop: (DragData) -> Unit,
    zIndex: Float,
    content: @Composable BoxScope.(isInBound: Boolean, dragOffset: Offset) -> Unit
) {
    val dndState = LocalDragAndDropState.current
    val dragPosition = dndState.itemPosition
    val dragOffset = dndState.dragOffset
    var isAboveDropSurface by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(isAboveDropSurface) {
        if (isAboveDropSurface) dndState.onDropZoneEnter(onDrop, zIndex)
        else dndState.onDropZoneLeave(onDrop, zIndex)
    }
    Box(
        modifier = modifier.onGloballyPositioned {
            it.boundsInWindow().let { rect ->
                isAboveDropSurface = dndState.isDragging && rect.contains(dragPosition + dragOffset)
            }
        }
    ) {
        content(isAboveDropSurface, dragOffset)
    }
}