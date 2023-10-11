package com.example.schedule.presentation.schedule

import com.example.schedule.presentation.schedule.model.ScheduleDayModel
import java.time.LocalDate

data class ScheduleState(
    val schedule: List<ScheduleDayModel> = emptyList(),
)

sealed class ScheduleNavEvent {
    class OpenGoalDialogWithGoal(val goalId: Int) : ScheduleNavEvent()
    class OpenGoalDialogWithDate(val epochDay: Long) : ScheduleNavEvent()
}

sealed class ScheduleEvent {
    class AddGoalToScheduleDayClick(val epochDay: Long) : ScheduleEvent()
    class GoalClick(val goalId: Int) : ScheduleEvent()
    class CompleteGoal(val goalId: Int) : ScheduleEvent()
    class GoalDropOnSchedule(
        val goalId: Int,
        val date: LocalDate,
        val isAppointment: Boolean
    ) : ScheduleEvent()
}

sealed class ScheduleEffect {
    class Error(val msgResource: Int) : ScheduleEffect()
}