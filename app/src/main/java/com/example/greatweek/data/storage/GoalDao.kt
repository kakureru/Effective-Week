package com.example.greatweek.data.storage

import androidx.room.Dao
import androidx.room.Query
import com.example.greatweek.data.storage.model.Goals
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals WHERE day = :dayId")
    fun getGoalsByDay(dayId: Int): Flow<List<Goals>>
}