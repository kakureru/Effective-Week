package com.example.schedule.domain.usecase.goal

import com.example.schedule.domain.repository.GoalRepository
import java.time.LocalDate

class DropGoalToWeekUseCase(private val goalRepository: GoalRepository) {
    suspend operator fun invoke(goalId: Int, date: LocalDate, isCommitment: Boolean) {
        val goal = goalRepository.getGoal(goalId = goalId).copy(date = date, appointment = isCommitment)
        goalRepository.editGoal(goal)
    }
}