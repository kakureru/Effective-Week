package com.example.greatweek.domain.usecase.role

import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.repository.GoalRepository
import com.example.greatweek.domain.repository.RoleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetRolesWithGoalsUseCase(
    private val roleRepository: RoleRepository,
    private val goalRepository: GoalRepository
) {
    fun execute(): Flow<List<Role>> {
        val roleFlow = roleRepository.allRoles
        val goalFlow = goalRepository.allGoals
        return roleFlow.combine(goalFlow) { roleList, goalList ->
            roleList.forEach { role ->
                role.goals = goalList.filter { goal ->
                    goal.role == role.name
                }
            }
            roleList
        }
    }
}