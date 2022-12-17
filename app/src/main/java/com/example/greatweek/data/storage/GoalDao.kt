package com.example.greatweek.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.greatweek.data.storage.model.Goals
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals WHERE day = :dayId")
    fun getGoalsByDay(dayId: Int): Flow<List<Goals>>

    @Query("SELECT * FROM goals")
    fun getGoals(): Flow<List<Goals>>

    @Insert
    fun addGoal(goal: Goals)

    @Query("DELETE FROM goals WHERE id = :goalId")
    fun completeGoal(goalId: Int)

    @Query("UPDATE goals SET " +
            "title = :title, " +
            "description = :description, " +
            "role_id = :roleId, " +
            "day = :day, " +
            "commitment = :commitment " +
            "WHERE id = :id")
    fun editGoal(
        id: Int,
        title: String,
        description: String,
        roleId: Int,
        day: Int,
        commitment: Boolean
    )

    @Query("SELECT * FROM goals WHERE id = :goalId")
    fun getGoalById(goalId: Int): Goals
}