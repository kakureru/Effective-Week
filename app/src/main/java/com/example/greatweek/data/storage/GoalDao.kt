package com.example.greatweek.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.greatweek.data.storage.model.Goals
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals")
    fun getGoals(): Flow<List<Goals>>

    @Insert
    suspend fun addGoal(goal: Goals)

    @Query("DELETE FROM goals WHERE id = :goalId")
    suspend fun completeGoal(goalId: Int)

    @Update
    suspend fun updateGoal(goal: Goals)

    @Query("SELECT * FROM goals WHERE id = :goalId")
    suspend fun getGoalById(goalId: Int): Goals
}