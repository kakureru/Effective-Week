package com.example.greatweek.ui.screens.schedule

import com.example.greatweek.domain.model.Role
import com.example.greatweek.ui.screens.schedule.model.WeekDayItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.time.LocalDate

data class ScheduleState(
    val schedule: List<WeekDayItem> = emptyList(),
    val currentDate: LocalDate = LocalDate.now(),
    val roles: List<Role> = emptyList(),
    val expanded: Int = BottomSheetBehavior.STATE_COLLAPSED,
)

sealed class ScheduleEvent {
    class CompleteGoal(val goalId: Int) : ScheduleEvent()
    class DeleteRole(val role: String) : ScheduleEvent()
    class GoalDropOnSchedule(val goalId: Int, val date: LocalDate, val isCommitment: Boolean) : ScheduleEvent()
    object DragRight : ScheduleEvent()
    object DragLeft : ScheduleEvent()
    object DragIdle : ScheduleEvent()
    class GoalDropOnRole(val goalId: Int, val role: String) : ScheduleEvent()
//    object Collapse : ScheduleEvent()
//    object Expand : ScheduleEvent()
}