package com.effectiveweek.schedule.presentation.schedule

import com.effectiveweek.schedule.presentation.schedule.model.ScheduleDayModel
import java.time.LocalDate

data class ScheduleUiState(
    val month: String = "",
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
    class FirstVisibleDayIndexChange(val newIndex: Int) : ScheduleEvent()
    class LastVisibleDayIndexChange(val newIndex: Int) : ScheduleEvent()
    data object TodayClick : ScheduleEvent()
}

sealed class ScheduleEffect {
    class Error(val msgResource: Int) : ScheduleEffect()
    class ScrollToDay(val index: Int) : ScheduleEffect()
}