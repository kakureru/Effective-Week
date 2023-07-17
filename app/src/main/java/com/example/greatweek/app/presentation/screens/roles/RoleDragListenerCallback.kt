package com.example.greatweek.app.presentation.screens.roles

import android.view.DragEvent
import android.view.View

interface RoleDragListenerCallback {
    fun onDragEntered(view: View): Boolean
    fun onDragExited(view: View): Boolean
    fun onDrop(view: View, event: DragEvent, role: String): Boolean
    fun onDragEnded(view: View, event: DragEvent): Boolean
}