package com.example.core.ui.draganddrop

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

class DragAndDropState {

    private val lock = Any()

    /**
     * The composable or piece of UI or simply an item that can be dragged around
     */
    var draggableItem by mutableStateOf<(@Composable () -> Unit)?>(null)

    /**
     * Whether the drag gesture has been detected on the item
     */
    var isDragging by mutableStateOf(false)

    /**
     * Current position of the item to be dragged
     */
    var itemPosition by mutableStateOf(Offset.Zero)

    /**
     * Current position of the drag pointer after drag is started
     */
    var dragOffset by mutableStateOf(Offset.Zero)


    private var dropCallbacks: MutableSet<DropCallback> = mutableSetOf()

    fun addDropCallback(callback: DropCallback) {
        synchronized(lock) {
            dropCallbacks.add(callback)
            Log.d("MYTAG", "add ${dropCallbacks.size}")
        }
    }

    fun removeDropCallback(callback: DropCallback) {
        synchronized(lock) {
            dropCallbacks.remove(callback)
            Log.d("MYTAG", "remove ${dropCallbacks.size}")
        }
    }

    fun executeDropCallbacks(dragData: DragData) {
        synchronized(lock) {
            Log.d("MYTAG", "execute ${dropCallbacks.size}")
            dropCallbacks.forEach {
                it.invoke(dragData)
            }
            dropCallbacks.clear()
            Log.d("MYTAG", "clear ${dropCallbacks.size}")
        }
    }

    // Metadata
    var cardDraggedId by mutableStateOf(-1)
}

internal val LocalDragAndDropState = compositionLocalOf { DragAndDropState() }

typealias DropCallback = (DragData) -> Unit
interface DragData {
    val id: Int
}