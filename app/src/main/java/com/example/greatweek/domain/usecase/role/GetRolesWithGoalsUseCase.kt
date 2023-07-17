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
    operator fun invoke(): Flow<List<Role>> {
        return roleRepository.allRoles.combine(goalRepository.allGoals) { roleList, goalList ->
            roleList.map { role ->
                Role(
                    name = role.name,
                    goals = goalList.filter { goal ->
                        goal.role == role.name && goal.date == null
                    }
                )
            }
        }
    }
}