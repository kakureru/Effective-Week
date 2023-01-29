package com.example.greatweek.data.repository

import com.example.greatweek.data.db.DataVersionDao
import com.example.greatweek.data.model.db.DataVersion
import com.example.greatweek.domain.repository.DataVersionRepository
import java.time.LocalDateTime

class DataVersionRepositoryImpl(
    private val dataVersionDao: DataVersionDao
) : DataVersionRepository {

    override suspend fun addDataVersion() {
        val newDataVersion = DataVersion(
            date = LocalDateTime.now()
        )
        dataVersionDao.addDataVersion(newDataVersion)
    }
}