package com.example.greatweek.ui.screens.goaldialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.greatweek.App
import com.example.greatweek.R
import com.example.greatweek.databinding.GoalDialogLayoutBinding
import com.example.greatweek.ui.collectFlowSafely
import com.example.greatweek.ui.screens.goaldialog.dialogdata.DateDialogData
import com.example.greatweek.ui.screens.goaldialog.dialogdata.RoleDialogData
import com.example.greatweek.ui.screens.goaldialog.dialogdata.TimeDialogData
import com.example.greatweek.ui.screens.goaldialog.rolepicker.RolePickerDialogFragment
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import javax.inject.Inject

class GoalDialogFragment : DialogFragment() {

    private val id: Int? by lazy { requireArguments().getInt(ARG_ID, -1).takeIf { it > -1 } }
    private val date: LocalDate? by lazy { requireArguments().get(ARG_DATE) as LocalDate? }
    private val role: String? by lazy { requireArguments().getString(ARG_ROLE) }

    @Inject lateinit var viewModelFactory: GoalDialogViewModelFactory.Factory
    private val viewModel: GoalDialogViewModel by viewModels { viewModelFactory.create(id, date, role) }

    private lateinit var binding: GoalDialogLayoutBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = GoalDialogLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext()).setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            etTitle.addTextChangedListener {
                viewModel.accept(GoalDialogEvent.TitleChanged(etTitle.text.toString()))
            }
            etDescription.addTextChangedListener {
                viewModel.accept(GoalDialogEvent.DescriptionChanged(etDescription.text.toString()))
            }

            btnRole.setOnClickListener { viewModel.accept(GoalDialogEvent.RoleClick) }
            btnDate.setOnClickListener { viewModel.accept(GoalDialogEvent.DateClick) }
            btnTime.setOnClickListener { viewModel.accept(GoalDialogEvent.TimeClick) }

            cbAppointment.setOnCheckedChangeListener { _, isChecked ->
                viewModel.accept(GoalDialogEvent.AppointmentCheckChanged(isChecked))
            }

            btnConfirm.setOnClickListener { viewModel.accept(GoalDialogEvent.ConfirmClick) }
            btnDismiss.setOnClickListener { dismiss() }
        }

        viewModel.uiState.render()
        viewModel.uiEffect.handleEffect()
        return dialog
    }

    private fun StateFlow<GoalDialogState>.render() = collectFlowSafely {
        collect { state ->
            if (state.isSuccessful) dismiss()
            binding.apply {
                if (etTitle.text.isEmpty()) etTitle.setText(state.title)
                if (etDescription.text.isEmpty()) etDescription.setText(state.description)
                btnRole.text = state.role ?: requireContext().getString(R.string.role)
                btnDate.text = state.date ?: requireContext().getString(R.string.date)
                btnTime.text = state.time ?: requireContext().getString(R.string.time)
                cbAppointment.isChecked = state.appointment
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