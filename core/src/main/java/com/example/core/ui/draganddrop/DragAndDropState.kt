package com.example.core.ui.draganddrop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

interface DragData {
    val id: Int
}

typealias DropCallback = (DragData) -> Unit

internal val LocalDragAndDropState = compositionLocalOf { DragAndDropState() }

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


    fun onDropZoneEnter(callback: DropCallback, zIndex: Float) {
        addDropCallback(callback, zIndex)
    }

    fun onDropZoneLeave(callback: DropCallback, zIndex: Float) {
        removeDropCallback(callback, zIndex)
    }

    fun onDrop(dragData: DragData) {
        executeDropCallbacks(dragData)
    }

    private var dropCallbacks: MutableMap<Float, MutableSet<DropCallback>> = mutableMapOf()

    private fun addDropCallback(callback: DropCallback, zIndex: Float) {
        synchronized(lock) {
            dropCallbacks[zIndex]?.add(callback) ?: run {
                dropCallbacks[zIndex] = mutableSetOf(callback)
            }
        }
    }

    private fun removeDropCallback(callback: DropCallback, zIndex: Float) {
        synchronized(lock) {
            dropCallbacks[zIndex]?.remove(callback)
        }
    }

    private fun executeDropCallbacks(dragData: DragData) {
        synchronized(lock) {
            dropCallbacks
                .filter { it.value.isNotEmpty() }
                .maxBy { it.key }
                .value
                .forEach { it.invoke(dragData) }
            dropCallbacks.clear()
        }
    }

    // Metadata
    var cardDraggedId by mutableStateOf(-1)
}