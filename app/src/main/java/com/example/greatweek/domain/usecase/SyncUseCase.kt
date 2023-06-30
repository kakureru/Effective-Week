package com.example.greatweek.domain.usecase

import com.example.greatweek.domain.SyncManager

class SyncUseCase(private val syncManager: SyncManager) {
    operator fun invoke() = syncManager.syncData()
}