package com.example.greatweek.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.greatweek.data.db.model.DataVersion
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(Converters::class)
interface DataVersionDao {

    @Query("SELECT * FROM data_version ORDER BY id DESC LIMIT 1")
    fun getLatestVersion(): Flow<DataVersion>

    @Insert
    suspend fun addDataVersion(dataVersion: DataVersion)
}