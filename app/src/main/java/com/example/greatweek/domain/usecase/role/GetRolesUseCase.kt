package com.example.greatweek.domain.usecase.role

import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.repository.GoalRepository
import com.example.greatweek.domain.repository.RoleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetRolesUseCase(
    private val roleRepository: RoleRepository,
    private val goalRepository: GoalRepository
) {
    fun execute(): Flow<List<Role>> {
        return roleRepository.getRoles()
            .combine(goalRepository.getGoals()) { roleList, goalList ->
                roleList.forEach { it.goals = goalList.filter { goal -> goal.roleId == it.id } }
                roleList
            }
    }
}