package com.effectiveweek.core.ui.draganddrop

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitLongPressOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun DragSurface(
    modifier: Modifier = Modifier,
    cardId: Int = 0,
    dragData: DragData,
    content: @Composable () -> Unit
) {
    val dndState = LocalDragAndDropState.current
    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    var targetHeight by remember { mutableStateOf(0) }
    Box(
        modifier = modifier
            .onGloballyPositioned {
                currentPosition = it.localToWindow(Offset.Zero)
                targetHeight = it.size.height
            }
            .pointerInput(key1 = cardId) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val longPress = awaitLongPressOrCancellation(down.id)
                    if (longPress != null) {
                        with(dndState) {
                            isDragging = true
                            dragPosition = currentPosition + longPress.position
                            this.dragData = dragData
                            draggableItem = content

                            // Metadata
                            cardDraggedId = cardId
                        }
                    }
                }
            }
    ) {
        // Experimental animation to show empty space while dragging
        if (dndState.isDragging && dndState.cardDraggedId == cardId) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(LocalDensity.current.run { targetHeight.toDp() })
//            )
        } else {
            content()
        }
    }
}