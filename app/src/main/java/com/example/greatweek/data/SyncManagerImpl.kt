package com.example.greatweek.data

import com.example.greatweek.domain.SyncManager
import com.example.greatweek.domain.repository.DataVersionRepository
import com.example.greatweek.domain.repository.GoalRepository
import com.example.greatweek.domain.repository.RoleRepository
import com.example.greatweek.domain.repository.UserRepository

class SyncManagerImpl(
    private val roleRepository: RoleRepository,
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val dataVersionRepository: DataVersionRepository
) : SyncManager {

    override fun syncData() {
        TODO("Отправка данных")
    }
}