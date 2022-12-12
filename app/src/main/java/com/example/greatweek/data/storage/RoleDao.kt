package com.example.greatweek.data.storage

import androidx.room.*
import com.example.greatweek.data.storage.model.Roles
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRole(role: Roles)

    @Query("SELECT * FROM roles")
    fun getRoles(): Flow<List<Roles>>

    @Query("DELETE FROM roles WHERE id = :roleId")
    fun deleteRole(roleId: Int)

    @Query("UPDATE roles SET name = :newName WHERE id = :roleId")
    fun renameRole(roleId: Int, newName: String)
}