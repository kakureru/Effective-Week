package com.example.greatweek.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.greatweek.data.storage.model.Goals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals WHERE day = :dayId")
    fun getGoalsByDay(dayId: Int): Flow<List<Goals>>

    @Query("SELECT * FROM goals")
    fun getGoals(): Flow<List<Goals>>

    @Insert
    fun addGoal(goal: Goals)
}