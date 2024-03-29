package com.effectiveweek.schedule.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.effectiveweek.schedule.data.db.model.RoleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleDao {

    @Query("SELECT * FROM roles")
    fun getRoles(): Flow<List<RoleEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRole(role: RoleEntity)

    @Query("DELETE FROM roles WHERE name = :name")
    suspend fun deleteRole(name: String)

    @Query("UPDATE roles SET name = :newName WHERE name = :oldName")
    suspend fun updateRole(oldName: String, newName: String)

    @Query("SELECT * FROM roles WHERE name = :name")
    suspend fun getRole(name: String): RoleEntity
}