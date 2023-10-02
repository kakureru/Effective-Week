package com.example.schedule.presentation.schedule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.domain.model.Role
import com.example.schedule.presentation.goal_dialog.GoalDialogFragment
import com.example.schedule.presentation.role_dialog.RoleDialogFragment
import com.example.schedule.presentation.schedule.model.GoalCallback
import com.example.schedule.presentation.schedule.ui.ScheduleScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class ScheduleFragment : Fragment() {

    private val viewModel: ScheduleViewModel by viewModel()

    private val goalCallback = object : GoalCallback {
        override fun onCompleteClick(goalId: Int) = viewModel.accept(ScheduleEvent.CompleteGoal(goalId))
        override fun onClick(goalId: Int) = openEditGoalDialog(goalId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DarkTheme {
                    Surface(color = Color.Black) {
                        ScheduleScreen(
                            viewModel = viewModel,
                            goalCallback = goalCallback,
                            onAddGoalToScheduleClick = { date -> openAddGoalDialog(date) },
                            onDeleteRoleClick = { role -> onDeleteClick(role) },
                            onEditRoleClick = { role -> openRoleDialog(role) },
                            onAddGoalToRoleClick = { roleName -> openAddGoalDialog(roleName) },
                            onAddRoleClick = { openRoleDialog() }
                        )
                    }
                }
            }
        }
    }

    private fun onDeleteClick(role: Role) {
        if (role.goals.isNotEmpty())
            showRoleWarning(requireContext())
        else
            viewModel.accept(ScheduleEvent.DeleteRole(role.name))
    }

    private fun showRoleWarning(context: Context) {
        Toast.makeText(context, "Can't delete role with active goals", Toast.LENGTH_SHORT).show()
    }

    private fun openAddGoalDialog(role: String) =
        GoalDialogFragment.showForRole(
            manager = parentFragmentManager,
            role = role,
        )

    private fun openRoleDialog(role: Role) =
        RoleDialogFragment.show(
            manager = parentFragmentManager,
            roleName = role.name
        )

    private fun openRoleDialog() =
        RoleDialogFragment.show(
            manager = parentFragmentManager
        )

    private fun openEditGoalDialog(goalId: Int) {
        GoalDialogFragment.showForGoal(
            manager = parentFragmentManager,
            goalId = goalId,
        )
    }

    private fun openAddGoalDialog(date: LocalDate) {
        GoalDialogFragment.showForDate(
            manager = parentFragmentManager,
            date = date,
        )
    }
}