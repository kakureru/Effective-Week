package com.example.greatweek.data.storage

import androidx.room.*
import com.example.greatweek.data.model.Goals
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals")
    fun getAll(): Flow<List<Goals>>

    @Query("SELECT * FROM goals WHERE date BETWEEN :firstDay AND :lastDay")
    @TypeConverters(Converters::class)
    fun getGoals(firstDay: LocalDate, lastDay: LocalDate): Flow<List<Goals>>

    @Insert
    suspend fun addGoal(goal: Goals)

    @Query("DELETE FROM goals WHERE id = :goalId")
    suspend fun completeGoal(goalId: Int)

    @Update
    suspend fun updateGoal(goal: Goals)

    @Query("SELECT * FROM goals WHERE id = :goalId")
    suspend fun getGoalById(goalId: Int): Goals
}