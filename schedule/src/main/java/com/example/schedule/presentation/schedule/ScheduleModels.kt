package com.example.schedule.presentation.schedule

import com.example.schedule.domain.model.Role
import com.example.schedule.presentation.schedule.model.ScheduleDayModel
import java.time.LocalDate

data class ScheduleState(
    val schedule: List<ScheduleDayModel> = emptyList(),
    val currentDate: LocalDate = LocalDate.now(),
    val roles: List<Role> = emptyList(),
)

sealed class ScheduleNavEvent {
    object OpenRoleDialog : ScheduleNavEvent()
    class OpenRoleDialogWithRole(val roleName: String) : ScheduleNavEvent()
    class OpenGoalDialogWithGoal(val goalId: Int) : ScheduleNavEvent()
    class OpenGoalDialogWithDate(val epochDay: Long) : ScheduleNavEvent()
    class OpenGoalDialogWithRole(val roleName: String) : ScheduleNavEvent()
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
        val isAppointment: Boolean
    ) : ScheduleEvent()

    object DragRight : ScheduleEvent()
    object DragLeft : ScheduleEvent()
    object DragIdle : ScheduleEvent()
    class GoalDropOnRole(val goalId: Int, val role: String) : ScheduleEvent()
}

sealed class ScheduleEffect {
    class Error(val msgResource: Int) : ScheduleEffect()
}