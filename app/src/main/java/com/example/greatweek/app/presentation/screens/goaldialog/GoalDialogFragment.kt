package com.example.greatweek.app.presentation.screens.goaldialog

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.greatweek.R
import com.example.greatweek.app.App
import com.example.greatweek.app.presentation.ViewModelFactory
import com.example.greatweek.app.presentation.constants.KEY_ADD_GOAL_FOR_A_DAY_REQUEST_KEY
import com.example.greatweek.app.presentation.constants.KEY_ADD_GOAL_FOR_A_ROLE_REQUEST_KEY
import com.example.greatweek.app.presentation.constants.KEY_EDIT_GOAL_REQUEST_KEY
import com.example.greatweek.databinding.GoalDialogLayoutBinding
import com.example.greatweek.databinding.RoleBottomSheetDialogLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

class GoalDialogFragment : DialogFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: GoalDialogFragmentViewModel by viewModels { viewModelFactory }

    private val requestKey: String
        get() = requireArguments().getString(ARG_REQUEST_KEY)!!

    private lateinit var binding: GoalDialogLayoutBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (activity?.applicationContext as App).appComponent.inject(this)

        binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.goal_dialog_layout, null, false
        )
        binding.goalDialogFragment = this@GoalDialogFragment

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        when (requestKey) {
            KEY_EDIT_GOAL_REQUEST_KEY -> {
                viewModel.setId(requireArguments().getInt(ARG_ARGUMENT))
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.getGoal()
                    bind()
                }
            }

            KEY_ADD_GOAL_FOR_A_DAY_REQUEST_KEY -> {
                viewModel.setDate(requireArguments().get(ARG_ARGUMENT) as LocalDate)
                bind()
            }

            KEY_ADD_GOAL_FOR_A_ROLE_REQUEST_KEY -> {
                viewModel.setRole(requireArguments().getString(ARG_ARGUMENT).toString())
                bind()
            }
        }

        binding.confirmButton.setOnClickListener {
            if (binding.titleEditText.text.toString().isBlank()) {
                binding.titleEditText.error = getString(R.string.empty_value)
                return@setOnClickListener
            }
            if (viewModel.role == null) {
                Toast.makeText(requireContext(), "Choose role", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.setGoal(
                binding.titleEditText.text.toString(),
                binding.descriptionEditText.text.toString(),
                binding.commitmentCheckBox.isChecked
            )
            when (requestKey) {
                KEY_EDIT_GOAL_REQUEST_KEY -> viewModel.editGoal()
                else -> viewModel.addGoal()
            }
            dismiss()
        }

        binding.dismissButton.setOnClickListener {
            dismiss()
        }

        return dialog
    }

    private fun bind() {
        binding.apply {
            if (titleEditText.text.isEmpty())
                titleEditText.setText(viewModel.title)
            if (descriptionEditText.text.isEmpty())
                descriptionEditText.setText(viewModel.description)
            viewModel.date?.let {
                dateButton.text = DateTimeFormatter.ofPattern("MMM d").format(viewModel.date)
            }
            viewModel.time?.let {
                timeButton.text = DateUtils.formatDateTime(
                    requireContext(),
                    viewModel.calendar.timeInMillis,
                    DateUtils.FORMAT_SHOW_TIME
                )
            }
            viewModel.role?.let { roleButton.text = viewModel.role }
            commitmentCheckBox.isChecked = viewModel.commitment
        }
    }

    fun showRoleDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding = RoleBottomSheetDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogBinding.root)

        val roleAdapter = RoleBottomSheetDialogAdapter { role ->
            viewModel.setRole(role.name)
            binding.roleButton.text = viewModel.role
            bottomSheetDialog.dismiss()
        }
        dialogBinding.roleRecyclerView.adapter = roleAdapter
        lifecycle.coroutineScope.launch {
            viewModel.getRoles().collect {
                roleAdapter.submitList(it)
            }
        }

        bottomSheetDialog.show()
    }

    private val timeCallBack = OnTimeSetListener { _, h, m ->
        viewModel.setTime(h, m)
        bind()
    }

    private val dateCallBack = OnDateSetListener { _, y, m, d ->
        viewModel.setDate(y, m, d)
        bind()
    }

    fun showTimeDialog() {
        TimePickerDialog(
            requireContext(), timeCallBack,
            viewModel.calendar.get(Calendar.HOUR_OF_DAY),
            viewModel.calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    fun showDateDialog() {
        DatePickerDialog(
            requireContext(), dateCallBack,
            viewModel.calendar.get(Calendar.YEAR),
            viewModel.calendar.get(Calendar.MONTH),
            viewModel.calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    companion object {
        private val TAG = GoalDialogFragment::class.java.simpleName
        private const val ARG_ARGUMENT = "ARG_ARGUMENT"
        private const val ARG_REQUEST_KEY = "ARG_REQUEST_KEY"

        fun show(
            manager: FragmentManager,
            argument: Int,
            requestKey: String
        ) {
            val dialogFragment = GoalDialogFragment()
            dialogFragment.arguments = bundleOf(
                ARG_ARGUMENT to argument,
                ARG_REQUEST_KEY to requestKey
            )
            dialogFragment.show(manager, TAG)
        }

        fun show(
            manager: FragmentManager,
            argument: LocalDate,
            requestKey: String
        ) {
            val dialogFragment = GoalDialogFragment()
            dialogFragment.arguments = bundleOf(
                ARG_ARGUMENT to argument,
                ARG_REQUEST_KEY to requestKey
            )
            dialogFragment.show(manager, TAG)
        }

        fun show(
            manager: FragmentManager,
            argument: String, //!!!!!!!!!!!
            requestKey: String
        ) {
            val dialogFragment = GoalDialogFragment()
            dialogFragment.arguments = bundleOf(
                ARG_ARGUMENT to argument,
                ARG_REQUEST_KEY to requestKey
            )
            dialogFragment.show(manager, TAG)
        }
    }
}