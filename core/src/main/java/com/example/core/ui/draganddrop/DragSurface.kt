package com.example.core.ui.draganddrop

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

@Composable
fun DragSurface(
    modifier: Modifier = Modifier,
    cardId: Int = 0,
    content: @Composable () -> Unit
) {
    val dragNDropState = LocalDragAndDropState.current
    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    var targetHeight by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .onGloballyPositioned {
                currentPosition = it.localToWindow(Offset.Zero)
                targetHeight = it.size.height
            }
            .pointerInput(key1 = cardId) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        with(dragNDropState) {
                            isDragging = true
                            itemPosition = currentPosition + it
                            draggableItem = content

                            // Metadata
                            cardDraggedId = cardId
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragNDropState.dragOffset += Offset(dragAmount.x, dragAmount.y)
                    },
                    onDragEnd = {
                        with(dragNDropState) {
                            isDragging = false
                            dragOffset = Offset.Zero


                        }
                    },
                    onDragCancel = {
                        with(dragNDropState) {
                            isDragging = false
                            dragOffset = Offset.Zero

                            // Metadata
                            cardDraggedId = -1
                        }
                    }
                )
            }
    ) {
        // Experimental animation to show empty space while dragging
        if (dragNDropState.isDragging && dragNDropState.cardDraggedId == cardId) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LocalDensity.current.run { targetHeight.toDp() })
            )
        } else {
            content()
        }
    }
}