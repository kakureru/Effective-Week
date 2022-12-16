package com.example.greatweek.presentation.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import com.example.greatweek.R
import com.example.greatweek.data.repository.GoalRepositoryImpl
import com.example.greatweek.data.repository.RoleRepositoryImpl
import com.example.greatweek.databinding.GoalDialogLayoutBinding
import com.example.greatweek.databinding.RoleBottomSheetDialogLayoutBinding
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.usecase.goal.AddGoalUseCase
import com.example.greatweek.domain.usecase.role.GetRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesUseCase
import com.example.greatweek.presentation.Constants
import com.example.greatweek.presentation.GreatWeekApplication
import com.example.greatweek.presentation.adapter.RoleBottomSheetDialogAdapter
import com.example.greatweek.presentation.viewmodel.GoalDialogFragmentViewModel
import com.example.greatweek.presentation.viewmodel.GoalDialogFragmentViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GoalDialogFragment : DialogFragment() {

    // repository
    private val goalRepository by lazy {
        GoalRepositoryImpl(
            (activity?.application as GreatWeekApplication).database.GoalDao()
        )
    }
    private val roleRepository by lazy {
        RoleRepositoryImpl(
            (activity?.application as GreatWeekApplication).database.RoleDao()
        )
    }

    // use cases
    private val addGoalUseCase by lazy { AddGoalUseCase(goalRepository) }
    private val getRoleUseCase by lazy { GetRoleUseCase(roleRepository) }
    private val getRolesUseCase by lazy { GetRolesUseCase(roleRepository, goalRepository) }

    // viewModel
    private val viewModel: GoalDialogFragmentViewModel by activityViewModels {
        GoalDialogFragmentViewModelFactory(
            addGoalUseCase,
            getRoleUseCase,
            getRolesUseCase
        )
    }

    private var roleId: Int? = null

    private val requestKey: String
        get() = requireArguments().getString(ARG_REQUEST_KEY)!!

    private lateinit var binding: GoalDialogLayoutBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = GoalDialogLayoutBinding.inflate(layoutInflater)
        roleId = requireArguments().get(ARG_ROLE_ID) as Int?
        roleId?.let { setRole(roleId!!) }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.roleButton.setOnClickListener {
            showBottomSheetDialog()
        }

        binding.confirmButton.setOnClickListener {
            val newTitle = binding.titleEditText.text.toString()
            val newDescription = binding.descriptionEditText.text.toString()

            if (newTitle.isBlank()) {
                binding.titleEditText.error = getString(R.string.empty_value)
                return@setOnClickListener
            }
            if (roleId == null) {
                Toast.makeText(requireContext(), "Choose role", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (requestKey) {
                Constants.KEY_ADD_GOAL_REQUEST_KEY -> addGoal(
                    Goal(
                        title = newTitle,
                        description = newDescription,
                        roleId = roleId!!
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
        roleId = role.id
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
        private val ARG_ROLE_ID = "ARG_ROLE_ID"

        @JvmStatic
        private val ARG_REQUEST_KEY = "ARG_REQUEST_KEY"

        fun show(
            manager: FragmentManager,
            roleId: Int? = null,
            requestKey: String
        ) {
            val dialogFragment = GoalDialogFragment()
            dialogFragment.arguments = bundleOf(
                ARG_ROLE_ID to roleId,
                ARG_REQUEST_KEY to requestKey
            )
            dialogFragment.show(manager, TAG)
        }
    }
}