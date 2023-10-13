package com.effectiveweek.schedule.domain.usecase.role

import com.effectiveweek.schedule.domain.model.Role
import com.effectiveweek.schedule.domain.repository.GoalRepository
import com.effectiveweek.schedule.domain.repository.RoleRepository
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