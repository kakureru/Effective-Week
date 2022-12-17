package com.example.greatweek.app.presentation.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.coroutineScope
import com.example.greatweek.R
import com.example.greatweek.app.Constants
import com.example.greatweek.app.presentation.adapter.RoleBottomSheetDialogAdapter
import com.example.greatweek.app.presentation.viewmodel.GoalDialogFragmentViewModel
import com.example.greatweek.databinding.GoalDialogLayoutBinding
import com.example.greatweek.databinding.RoleBottomSheetDialogLayoutBinding
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.model.Role
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GoalDialogFragment : DialogFragment() {

    private val viewModel by viewModel<GoalDialogFragmentViewModel>()

    private var paramRoleId: Int? = null

    private val paramWeekDay: Int
        get() = requireArguments().getInt(ARG_WEEK_DAY)

    private val requestKey: String
        get() = requireArguments().getString(ARG_REQUEST_KEY)!!

    private lateinit var binding: GoalDialogLayoutBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = GoalDialogLayoutBinding.inflate(layoutInflater)
        paramRoleId = requireArguments().get(ARG_ROLE_ID) as Int?
        paramRoleId?.let { setRole(paramRoleId!!) }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.roleButton.setOnClickListener {
            showBottomSheetDialog()
        }

        binding.confirmButton.setOnClickListener {
            val paramTitle = binding.titleEditText.text.toString()
            val paramDescription = binding.descriptionEditText.text.toString()
            val paramCommitment = binding.commitmentCheckBox.isChecked

            if (paramTitle.isBlank()) {
                binding.titleEditText.error = getString(R.string.empty_value)
                return@setOnClickListener
            }
            if (paramRoleId == null) {
                Toast.makeText(requireContext(), "Choose role", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (requestKey) {
                Constants.KEY_ADD_GOAL_REQUEST_KEY -> addGoal(
                    Goal(
                        title = paramTitle,
                        description = paramDescription,
                        roleId = paramRoleId!!,
                        weekday = paramWeekDay,
                        commitment = paramCommitment
                    )
                )
                //Constants.KEY_EDIT_GOAL_REQUEST_KEY ->
            }
            dismiss()
        }

        binding.dismissButton.setOnClickListener {
            dismiss()
        }

        return dialog
    }

    private fun addGoal(goal: Goal) {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.addGoal(goal = goal)
        }
    }

    private fun setRole(roleId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            binding.roleButton.text = viewModel.getRole(roleId = roleId).name
        }
    }

    private fun setRole(role: Role) {
        paramRoleId = role.id
        binding.roleButton.text = role.name
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding = RoleBottomSheetDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogBinding.root)

        val roleAdapter = RoleBottomSheetDialogAdapter { role ->
            setRole(role)
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
        private val ARG_WEEK_DAY = "ARG_WEEK_DAY"

        @JvmStatic
        private val ARG_ROLE_ID = "ARG_ROLE_ID"

        @JvmStatic
        private val ARG_REQUEST_KEY = "ARG_REQUEST_KEY"

        fun show(
            manager: FragmentManager,
            weekDay: Int = 0,
            roleId: Int? = null,
            requestKey: String
        ) {
            val dialogFragment = GoalDialogFragment()
            dialogFragment.arguments = bundleOf(
                ARG_WEEK_DAY to weekDay,
                ARG_ROLE_ID to roleId,
                ARG_REQUEST_KEY to requestKey
            )
            dialogFragment.show(manager, TAG)
        }
    }
}