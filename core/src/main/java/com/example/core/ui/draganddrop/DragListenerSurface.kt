package com.example.core.ui.draganddrop

import android.util.Log
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
import kotlinx.coroutines.CoroutineScope

@Composable
fun DragListenerSurface(
    onAbove: suspend CoroutineScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(isInBound: Boolean) -> Unit = {}
) {
    val dndState = LocalDragAndDropState.current
    val dragOffset = dndState.dragPosition
    var isAboveDropSurface by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(isAboveDropSurface) {
        while (isAboveDropSurface) {
            onAbove()
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