package com.example.schedule.domain.usecase.role

import com.example.schedule.domain.model.Role
import com.example.schedule.domain.repository.GoalRepository
import com.example.schedule.domain.repository.RoleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetRolesWithGoalsUseCase(
    private val roleRepository: RoleRepository,
    private val goalRepository: GoalRepository
) {
    operator fun invoke(): Flow<List<Role>> {
        return roleRepository.getRoles().combine(goalRepository.getGoals()) { roleList, goalList ->
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