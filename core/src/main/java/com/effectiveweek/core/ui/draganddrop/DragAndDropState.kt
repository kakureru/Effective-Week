package com.effectiveweek.core.ui.draganddrop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface DragData {
    val id: Int
}

typealias DropCallback = (DragData) -> Unit

val LocalDragAndDropState = compositionLocalOf { DragAndDropState() }

class DragAndDropState {

    private val dropLock = Any()
    private val aboveMutex = Mutex()

    private val dropCallbacks: MutableMap<Float, MutableSet<DropCallback>> = mutableMapOf()
    private val aboveLevels: MutableSet<Float> = mutableSetOf()

    /**
     * The composable or piece of UI or simply an item that can be dragged around
     */
    var draggableItem by mutableStateOf<(@Composable () -> Unit)?>(null)

    /**
     * Whether the drag gesture has been detected on the item
     */
    var isDragging by mutableStateOf(false)

    /**
     * Current position of the drag pointer after drag is started
     */
    var dragPosition by mutableStateOf(Offset.Zero)

    var dragData by mutableStateOf<DragData?>(null)

    fun onDropZoneEnter(callback: DropCallback, zIndex: Float) {
        addDropCallback(callback, zIndex)
    }

    fun onDropZoneLeave(callback: DropCallback, zIndex: Float) {
        removeDropCallback(callback, zIndex)
    }

    fun onAboveZoneLeave(zIndex: Float) {
        removeAboveLevel(zIndex)
    }

    fun onDrop() {
        dragData?.let {
            executeDropCallbacks(it)
        }
        dragData = null
        aboveLevels.clear()
    }

    suspend fun onAboveDropZone(callback: suspend () -> Unit, zIndex: Float) {
        aboveMutex.withLock {
            aboveLevels.add(zIndex)
            aboveLevels.maxOrNull()?.let {
                if (zIndex >= it) callback()
            }
        }
    }

    private fun addDropCallback(callback: DropCallback, zIndex: Float) {
        synchronized(dropLock) {
            dropCallbacks[zIndex]?.add(callback) ?: run {
                dropCallbacks[zIndex] = mutableSetOf(callback)
            }
        }
    }

    private fun removeDropCallback(callback: DropCallback, zIndex: Float) {
        synchronized(dropLock) {
            dropCallbacks[zIndex]?.remove(callback)
        }
    }

    private fun executeDropCallbacks(dragData: DragData) {
        synchronized(dropLock) {
            dropCallbacks
                .filter { it.value.isNotEmpty() }
                .maxByOrNull { it.key }
                ?.value
                ?.forEach { it.invoke(dragData) }
            dropCallbacks.clear()
        }
    }
    
    private fun removeAboveLevel(level: Float) {
        synchronized(aboveMutex) {
            aboveLevels.remove(level)
        }
    }

    // Metadata
    var cardDraggedId by mutableStateOf(-1)
}