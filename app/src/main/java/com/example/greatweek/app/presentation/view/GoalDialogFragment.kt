package com.example.greatweek.app.presentation.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.greatweek.R
import com.example.greatweek.app.Constants
import com.example.greatweek.app.presentation.adapter.RoleBottomSheetDialogAdapter
import com.example.greatweek.app.presentation.viewmodel.GoalDialogFragmentViewModel
import com.example.greatweek.databinding.GoalDialogLayoutBinding
import com.example.greatweek.databinding.RoleBottomSheetDialogLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GoalDialogFragment : DialogFragment() {

    private val viewModel by viewModel<GoalDialogFragmentViewModel>()

    private val requestKey: String
        get() = requireArguments().getString(ARG_REQUEST_KEY)!!

    private lateinit var binding: GoalDialogLayoutBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.goal_dialog_layout, null, false
        )
        binding.goalDialogFragment = this@GoalDialogFragment

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val argument = requireArguments().getInt(ARG_ARGUMENT)
        when (requestKey) {
            Constants.KEY_EDIT_GOAL_REQUEST_KEY -> {
                viewModel.setId(argument)
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.getGoal()
                    bind()
                }
            }
            Constants.KEY_ADD_GOAL_FOR_A_DAY_REQUEST_KEY -> {
                viewModel.setWeekDay(argument)
            }
            Constants.KEY_ADD_GOAL_FOR_A_ROLE_REQUEST_KEY -> {
                viewModel.setRoleId(argument)
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.getRole()
                    bind()
                }
            }
        }

        binding.confirmButton.setOnClickListener {
            if (binding.titleEditText.text.toString().isBlank()) {
                binding.titleEditText.error = getString(R.string.empty_value)
                return@setOnClickListener
            }
            if (viewModel.roleId == null) {
                Toast.makeText(requireContext(), "Choose role", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.setGoal(
                binding.titleEditText.text.toString(),
                binding.descriptionEditText.text.toString(),
                binding.commitmentCheckBox.isChecked
            )
            when (requestKey) {
                Constants.KEY_EDIT_GOAL_REQUEST_KEY -> viewModel.editGoal()
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
            titleEditText.setText(viewModel.title)
            descriptionEditText.setText(viewModel.description)
            roleButton.text = viewModel.roleName
            commitmentCheckBox.isChecked = viewModel.commitment
        }
    }

    fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding = RoleBottomSheetDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogBinding.root)

        val roleAdapter = RoleBottomSheetDialogAdapter { role ->
            viewModel.setRoleId(role.id)
            viewModel.setRoleName(role.name)
            binding.roleButton.text = viewModel.roleName
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

    companion object {
        @JvmStatic
        private val TAG = GoalDialogFragment::class.java.simpleName

        @JvmStatic
        private val ARG_ARGUMENT = "ARG_ARGUMENT"

        @JvmStatic
        private val ARG_REQUEST_KEY = "ARG_REQUEST_KEY"

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
    }
}