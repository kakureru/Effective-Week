package com.example.schedule.presentation.goal_dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.core.ui.theme.DarkTheme
import com.example.greatweek.ui.screens.goaldialog.dialogdata.DateDialogData
import com.example.greatweek.ui.screens.goaldialog.dialogdata.RoleDialogData
import com.example.greatweek.ui.screens.goaldialog.dialogdata.TimeDialogData
import com.example.schedule.presentation.goal_dialog.ui.GoalDialog
import com.example.schedule.presentation.role_pick_dialog.RolePickerDialogFragment
import com.example.utils.collectFlowSafely
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate

class GoalDialogFragment : DialogFragment() {

    private val id: Int? by lazy { requireArguments().getInt(ARG_ID, -1).takeIf { it > -1 } }
    private val date: LocalDate? by lazy { requireArguments().get(ARG_DATE) as LocalDate? }
    private val role: String? by lazy { requireArguments().getString(ARG_ROLE) }

    private val viewModel: GoalDialogViewModel by viewModel { parametersOf(id, date, role) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel.uiEffect.handleEffect()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DarkTheme {
                    GoalDialog(
                        vm = viewModel,
                        onConfirmClick = { viewModel.accept(GoalDialogEvent.ConfirmClick) },
                        onDismissRequest = { dismiss() },
                    )
                }
            }
        }
    }

    private fun SharedFlow<GoalDialogEffect>.handleEffect() = collectFlowSafely {
        collect { effect ->
            when (effect) {
                is GoalDialogEffect.Error -> showError(effect.msg)
                is GoalDialogEffect.RoleDialog -> showRoleDialog(effect.dialogData)
                is GoalDialogEffect.TimeDialog -> showTimeDialog(effect.dialogData)
                is GoalDialogEffect.DateDialog -> showDateDialog(effect.dialogData)
            }
        }
    }

    private fun showError(msg: Int) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun showRoleDialog(dialogData: RoleDialogData) = RolePickerDialogFragment.apply {
        setupListener(childFragmentManager, this@GoalDialogFragment) { role ->
            dialogData.onRolePickListener(role)
        }
    }.show(childFragmentManager)

    private fun showTimeDialog(dialogData: TimeDialogData) = TimePickerDialog(
        requireContext(),
        dialogData.onTimeSetListener,
        dialogData.hourOfDay,
        dialogData.minute,
        true
    ).show()

    private fun showDateDialog(dialogData: DateDialogData) = DatePickerDialog(
        requireContext(),
        dialogData.onDateSetListener,
        dialogData.year,
        dialogData.month,
        dialogData.dayOfMonth
    ).show()

    companion object {
        private val TAG = GoalDialogFragment::class.java.simpleName
        private const val ARG_ID = "ARG_ID"
        private const val ARG_DATE = "ARG_DATE"
        private const val ARG_ROLE = "ARG_ROLE"

        fun showForGoal(manager: FragmentManager, goalId: Int) {
            GoalDialogFragment().apply {
                arguments = bundleOf(ARG_ID to goalId)
            }.show(manager, TAG)
        }

        fun showForDate(manager: FragmentManager, date: LocalDate) {
            GoalDialogFragment().apply {
                arguments = bundleOf(ARG_DATE to date)
            }.show(manager, TAG)
        }

        fun showForRole(manager: FragmentManager, role: String) {
            GoalDialogFragment().apply {
                arguments = bundleOf(ARG_ROLE to role)
            }.show(manager, TAG)
        }
    }
}