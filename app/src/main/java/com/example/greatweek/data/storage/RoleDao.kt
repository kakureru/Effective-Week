package com.example.greatweek.data.storage

import androidx.room.*
import com.example.greatweek.data.storage.model.Roles
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleDao {

    @Query("SELECT * FROM roles")
    fun getRoles(): Flow<List<Roles>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRole(role: Roles)

    @Query("DELETE FROM roles WHERE id = :roleId")
    suspend fun deleteRole(roleId: Int)

    @Update
    suspend fun updateRole(role: Roles)

    @Query("SELECT * FROM roles WHERE id = :roleId")
    suspend fun getRole(roleId: Int): Roles
}