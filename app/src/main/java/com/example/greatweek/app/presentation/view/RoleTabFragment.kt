package com.example.greatweek.app.presentation.view

import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.greatweek.app.presentation.adapter.RoleAdapter
import com.example.greatweek.app.presentation.constants.KEY_ADD_GOAL_FOR_A_ROLE_REQUEST_KEY
import com.example.greatweek.app.presentation.constants.KEY_ADD_ROLE_REQUEST_KEY
import com.example.greatweek.app.presentation.constants.KEY_EDIT_GOAL_REQUEST_KEY
import com.example.greatweek.app.presentation.constants.KEY_RENAME_ROLE_REQUEST_KEY
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModel
import com.example.greatweek.databinding.FragmentRoleTabBinding
import com.example.greatweek.domain.model.Role
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback

class RoleTabFragment : Fragment() {

    private val viewModel by activityViewModels<ScheduleViewModel>()

    private var _binding: FragmentRoleTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val bottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> viewModel.collapseBottomSheet()
                BottomSheetBehavior.STATE_EXPANDED -> viewModel.expandBottomSheet()
                else -> {}
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    var flag = false
    private val dragListener = View.OnDragListener { _, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                viewModel.collapseBottomSheet()
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                val clipDescription = event.clipDescription.label.toString()
                if (clipDescription == "null")
                    flag = true
                viewModel.expandBottomSheet()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                if (flag)
                    viewModel.expandBottomSheet()
                flag = false
                true
            }
            else -> false
        }
    }

    @Suppress("RedundantNullableReturnType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoleTabBinding.inflate(inflater, container, false)
        registerForContextMenu(binding.rolesRecyclerView)

        bottomSheetBehavior =
            BottomSheetBehavior.from((parentFragment as ScheduleFragment).binding.bottomSheetLayout.root)
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        binding.root.setOnDragListener(dragListener)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.roleTabFragment = this@RoleTabFragment

        val roleAdapter = RoleAdapter(
            renameRole = { role -> openRenameRoleDialog(role) },
            deleteRole = { name -> viewModel.deleteRole(name = name) },
            addGoal = { role -> openAddGoalDialog(role) },
            completeGoal = { goalId -> viewModel.completeGoal(goalId = goalId) },
            editGoal = { goalId -> openEditGoalDialog(goalId) },
            dropGoal = { goalId, role -> viewModel.dropGoal(goalId, role) },
            expandBottomSheet = { viewModel.expandBottomSheet() }
        )
        binding.rolesRecyclerView.adapter = roleAdapter

        viewModel.allRoles.observe(viewLifecycleOwner) {
            roleAdapter.submitList(it)
        }

        viewModel.tabExpanded.observe(viewLifecycleOwner) {
            bottomSheetBehavior.state = it
        }
    }

    /**
     * Show dialog
     */

    private fun openEditGoalDialog(goalId: Int) {
        GoalDialogFragment.show(
            manager = parentFragmentManager,
            argument = goalId,
            requestKey = KEY_EDIT_GOAL_REQUEST_KEY
        )
    }

    private fun openAddGoalDialog(role: String) {
        GoalDialogFragment.show(
            manager = parentFragmentManager,
            argument = role,
            requestKey = KEY_ADD_GOAL_FOR_A_ROLE_REQUEST_KEY
        )
    }

    private fun openRenameRoleDialog(role: Role) {
        RoleDialogFragment.show(
            manager = parentFragmentManager,
            roleName = role.name,
            requestKey = KEY_RENAME_ROLE_REQUEST_KEY
        )
    }

    fun openAddRoleDialog() {
        RoleDialogFragment.show(
            manager = parentFragmentManager,
            requestKey = KEY_ADD_ROLE_REQUEST_KEY
        )
    }
}