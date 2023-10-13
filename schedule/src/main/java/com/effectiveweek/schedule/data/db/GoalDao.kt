package com.effectiveweek.schedule.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.effectiveweek.schedule.data.db.model.GoalEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals")
    fun getAll(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE date BETWEEN :firstDay AND :lastDay")
    @TypeConverters(Converters::class)
    fun getGoals(firstDay: LocalDate, lastDay: LocalDate): Flow<List<GoalEntity>>

    @Insert
    suspend fun addGoal(goal: GoalEntity)

    @Query("DELETE FROM goals WHERE id = :goalId")
    suspend fun completeGoal(goalId: Int)

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Query("SELECT * FROM goals WHERE id = :goalId")
    suspend fun getGoalById(goalId: Int): GoalEntity
}