package com.example.schedule.presentation.schedule

import com.example.schedule.domain.model.Role
import com.example.schedule.presentation.schedule.model.ScheduleDayItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.time.LocalDate

data class ScheduleState(
    val schedule: List<ScheduleDayItem> = emptyList(),
    val currentDate: LocalDate = LocalDate.now(),
    val roles: List<Role> = emptyList(),
    val expanded: Int = BottomSheetBehavior.STATE_COLLAPSED,
    val navState: ScheduleNavState = ScheduleNavState.Idle,
)

sealed class ScheduleNavState {
    object Idle : ScheduleNavState()
    object OpenRoleDialog : ScheduleNavState()
    class OpenRoleDialogWithRole(val roleName: String) : ScheduleNavState()
    class OpenGoalDialogWithGoal(val goalId: Int) : ScheduleNavState()
    class OpenGoalDialogWithDate(val epochDay: Long) : ScheduleNavState()
    class OpenGoalDialogWithRole(val roleName: String) : ScheduleNavState()
}

sealed class ScheduleEvent {
    class AddGoalToScheduleDayClick(val epochDay: Long) : ScheduleEvent()
    class AddGoalToRoleClick(val roleName: String) : ScheduleEvent()
    object AddRoleClick : ScheduleEvent()
    class EditRoleClick(val roleName: String) : ScheduleEvent()
    class DeleteRoleClick(val roleName: String) : ScheduleEvent()
    class GoalClick(val goalId: Int) : ScheduleEvent()
    class CompleteGoal(val goalId: Int) : ScheduleEvent()
    class GoalDropOnSchedule(
        val goalId: Int,
        val date: LocalDate,
        val isCommitment: Boolean
    ) : ScheduleEvent()

    object DragRight : ScheduleEvent()
    object DragLeft : ScheduleEvent()
    object DragIdle : ScheduleEvent()
    class GoalDropOnRole(val goalId: Int, val role: String) : ScheduleEvent()
}

sealed class ScheduleEffect {
    class Error(val msgResource: Int) : ScheduleEffect()
}