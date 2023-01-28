package com.example.greatweek.data

import com.example.greatweek.data.db.DataVersionDao
import com.example.greatweek.data.model.db.DataVersion
import java.time.LocalDateTime

class SyncManager(
    private val dataVersionDao: DataVersionDao
) {
    suspend fun addDataVersion() {
        val newDataVersion = DataVersion(
            date = LocalDateTime.now()
        )
        dataVersionDao.addDataVersion(newDataVersion)
    }
}