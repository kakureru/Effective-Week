package com.example.core.ui.draganddrop

import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

private const val topAppBarHeight = 160f // TODO find workaround

@Composable
fun DragAndDropSurface(
    modifier: Modifier = Modifier,
    dndState: DragAndDropState = remember { DragAndDropState() },
    content: @Composable BoxScope.() -> Unit
) {
    CompositionLocalProvider(
        LocalDragAndDropState provides dndState
    ) {
        Box(
            modifier = modifier
                .wrapContentWidth()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        var change = awaitDragOrCancellation(down.id)
                        if (dndState.isDragging) {
                            if (change != null && change.pressed) {
                                change.consume()

                                dndState.dragPosition = Offset(change.position.x, change.position.y + topAppBarHeight)
                            }
                            while (change != null && change.pressed) {
                                change = awaitDragOrCancellation(change.id)
                                if (change != null && change.pressed) {
                                    change.consume()
                                    dndState.dragPosition = Offset(change.position.x, change.position.y + topAppBarHeight)
                                }
                            }
                            if (change != null) {
                                change.consume()
                                with(dndState) {
                                    isDragging = false
                                    dragPosition = Offset.Zero
                                    dndState.onDrop()
                                    // Metadata
                                    cardDraggedId = -1
                                }
                            }
                        }
                    }
                }
        ) {
            content()
            if (dndState.isDragging) {
                DragShadow(dndState.dragPosition, dndState.draggableItem)
            }
        }
    }
}

@Composable
fun DragShadow(
    position: Offset,
    draggableItem: @Composable (() -> Unit)?,
) {
    var targetSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    Box(
        modifier = Modifier
            .width(240.dp)
            .rotate(5f)
            .graphicsLayer {
                scaleX = 0.9f
                scaleY = 0.9f
                alpha = if (targetSize == IntSize.Zero) 0f else 1f
                translationX = position.x.minus(targetSize.width / 2)
                translationY = position.y.minus((targetSize.height / 2 + topAppBarHeight))
                spotShadowColor = Color(0xFF111111)
                ambientShadowColor = Color(0xFF111111)
            }
            .onGloballyPositioned {
                targetSize = it.size
            }
    ) {
        draggableItem?.invoke()
    }
}