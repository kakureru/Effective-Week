package com.example.greatweek.ui.screens.schedule

import com.example.greatweek.ui.screens.schedule.model.WeekDayItem
import java.time.LocalDate

data class ScheduleState(
    val schedule: List<WeekDayItem> = emptyList(),
    val currentDate: LocalDate = LocalDate.now(),
)

sealed class ScheduleEvent {
    class CompleteGoal(val goalId: Int) : ScheduleEvent()
    class GoalDrop(val goalId: Int, val date: LocalDate, val isCommitment: Boolean) : ScheduleEvent()
    object DragRight : ScheduleEvent()
    object DragLeft : ScheduleEvent()
    object DragIdle : ScheduleEvent()
}