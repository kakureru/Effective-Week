package com.example.greatweek.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.greatweek.data.storage.model.Roles

@Dao
interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRole(role: Roles)

    @Query("SELECT * FROM roles")
    fun getRoles(): List<Roles>
}