package com.example.greatweek.ui.screens.schedule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.greatweek.App
import com.example.greatweek.domain.model.Role
import com.example.greatweek.ui.ViewModelFactory
import com.example.greatweek.ui.screens.goaldialog.GoalDialogFragment
import com.example.greatweek.ui.screens.roledialog.RoleDialogFragment
import com.example.greatweek.ui.screens.schedule.model.GoalCallback
import com.example.greatweek.ui.screens.schedule.ui.ScheduleScreen
import com.example.greatweek.ui.theme.DarkTheme
import java.time.LocalDate
import javax.inject.Inject

class ScheduleFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ScheduleViewModel by viewModels { viewModelFactory }

    private val goalCallback = object : GoalCallback {
        override fun onCompleteClick(goalId: Int) = viewModel.accept(ScheduleEvent.CompleteGoal(goalId))
        override fun onClick(goalId: Int) = openEditGoalDialog(goalId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as App).appComponent.inject(this)
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
                            onEditRoleClick = { role -> openRenameRoleDialog(role) },
                            onAddGoalToRoleClick = { roleName -> openAddGoalDialog(roleName) },
                            onAddRoleClick = { openAddRoleDialog() }
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

    private fun openRenameRoleDialog(role: Role) =
        RoleDialogFragment.showForEdit(
            manager = parentFragmentManager,
            role = role.name
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

    fun openAddRoleDialog() = RoleDialogFragment.showForNew(manager = parentFragmentManager)
}