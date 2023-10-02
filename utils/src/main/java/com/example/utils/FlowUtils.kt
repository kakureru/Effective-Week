package com.example.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

fun LifecycleOwner.collectFlowSafely(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    collect: suspend () -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(lifecycleState) {
            collect()
        }
    }
}