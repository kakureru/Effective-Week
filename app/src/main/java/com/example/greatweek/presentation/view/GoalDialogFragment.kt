package com.example.greatweek.presentation.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.greatweek.R
import com.example.greatweek.data.repository.GoalRepositoryImpl
import com.example.greatweek.databinding.GoalDialogLayoutBinding
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.usecase.goal.AddGoalUseCase
import com.example.greatweek.presentation.Constants
import com.example.greatweek.presentation.GreatWeekApplication
import com.example.greatweek.presentation.viewmodel.GoalDialogFragmentViewModel
import com.example.greatweek.presentation.viewmodel.GoalDialogFragmentViewModelFactory
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

    // use cases
    private val addGoalUseCase by lazy { AddGoalUseCase(goalRepository) }

    // viewModel
    private val viewModel: GoalDialogFragmentViewModel by activityViewModels {
        GoalDialogFragmentViewModelFactory(addGoalUseCase)
    }

    private fun addGoal(goal: Goal) {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.addGoal(goal = goal)
        }
    }

    private val roleId: Int
        get() = requireArguments().getInt(ARG_ROLE_ID)

    private val requestKey: String
        get() = requireArguments().getString(ARG_REQUEST_KEY)!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = GoalDialogLayoutBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.confirmButton.setOnClickListener {
            val newTitle = dialogBinding.titleEditText.text.toString()
            val newDescription = dialogBinding.descriptionEditText.text.toString()
            if (newTitle.isBlank()) {
                dialogBinding.titleEditText.error = getString(R.string.empty_value)
                return@setOnClickListener
            }
            when (requestKey) {
                Constants.KEY_ADD_GOAL_REQUEST_KEY -> addGoal(
                    Goal(
                        title = newTitle,
                        description = newDescription,
                        roleId = roleId
                    )
                )
                //Constants.KEY_EDIT_GOAL_REQUEST_KEY ->
            }
            dismiss()
        }

        dialogBinding.dismissButton.setOnClickListener {
            dismiss()
        }

        return dialog
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
            roleId: Int = -1,
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