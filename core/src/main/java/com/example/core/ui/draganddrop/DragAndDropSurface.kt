package com.example.core.ui.draganddrop

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun DragAndDropSurface(
    modifier: Modifier = Modifier,
    state: DragAndDropState = remember { DragAndDropState() },
    content: @Composable BoxScope.() -> Unit
) {
    CompositionLocalProvider(
        LocalDragAndDropState provides state
    ) {
        Box(modifier = modifier.wrapContentWidth()) {
            content()
            if (state.isDragging) {
                var targetSize by remember {
                    mutableStateOf(IntSize.Zero)
                }
                Box(
                    modifier = Modifier
                        .width(240.dp)
                        .rotate(5f)
                        .graphicsLayer {
                            val offset = (state.itemPosition + state.dragOffset)
                            scaleX = 0.9f
                            scaleY = 0.9f
                            alpha = if (targetSize == IntSize.Zero) 0f else 1f
                            translationX = offset.x.minus(targetSize.width / 2)
                            // 160f is the height adjustment for top app bar, need to find a way to calculate this height
                            translationY = offset.y.minus((targetSize.height / 2 + 160f))
                            spotShadowColor = Color(0xFF111111)
                            ambientShadowColor = Color(0xFF111111)
                        }
                        .onGloballyPositioned {
                            targetSize = it.size
                        }
                ) {
                    state.draggableItem?.invoke()
                }
            }
        }
    }
}