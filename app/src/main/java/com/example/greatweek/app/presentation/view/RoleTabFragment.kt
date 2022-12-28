package com.example.greatweek.app.presentation.view

import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.greatweek.app.Constants
import com.example.greatweek.app.presentation.adapter.RoleAdapter
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModel
import com.example.greatweek.databinding.FragmentRoleTabBinding
import com.example.greatweek.domain.model.Role
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RoleTabFragment : Fragment() {

    private val viewModel by activityViewModel<ScheduleViewModel>()

    private var _binding: FragmentRoleTabBinding? = null
    private val binding get() = _binding!!

    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoleTabBinding.inflate(inflater, container, false)
        bottomSheetBehavior =
            BottomSheetBehavior.from((parentFragment as ScheduleFragment).binding.bottomSheetLayout.root)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> viewModel.collapseBottomSheet()
                    BottomSheetBehavior.STATE_EXPANDED -> viewModel.expandBottomSheet()
                    BottomSheetBehavior.STATE_DRAGGING -> { }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> { }
                    BottomSheetBehavior.STATE_HIDDEN -> { }
                    BottomSheetBehavior.STATE_SETTLING -> { }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var flag = false
        val dragListener = View.OnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> true
                DragEvent.ACTION_DRAG_EXITED -> {
                    viewModel.collapseBottomSheet()
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    val clipDescription = event.clipDescription.label.toString()
                    if (clipDescription.toInt() == 0)
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
        binding.root.setOnDragListener(dragListener)

        binding.apply {
            roleTabFragment = this@RoleTabFragment
        }
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
        viewModel.allRoles.observe(viewLifecycleOwner) { roles ->
            roles?.let { roleAdapter.submitList(roles) }
            roleAdapter.notifyDataSetChanged()
        }
        viewModel.tabExpanded.observe(viewLifecycleOwner) {
            bottomSheetBehavior.state = it
        }
        registerForContextMenu(binding.rolesRecyclerView)
    }

    /**
     * Show dialog
     */

    private fun openEditGoalDialog(goalId: Int) {
        GoalDialogFragment.show(
            manager = parentFragmentManager,
            argument = goalId,
            requestKey = Constants.KEY_EDIT_GOAL_REQUEST_KEY
        )
    }

    private fun openAddGoalDialog(role: String) {
        GoalDialogFragment.show(
            manager = parentFragmentManager,
            argument = role,
            requestKey = Constants.KEY_ADD_GOAL_FOR_A_ROLE_REQUEST_KEY
        )
    }

    private fun openRenameRoleDialog(role: Role) {
        RoleDialogFragment.show(
            manager = parentFragmentManager,
            roleName = role.name,
            requestKey = Constants.KEY_RENAME_ROLE_REQUEST_KEY
        )
    }

    fun openAddRoleDialog() {
        RoleDialogFragment.show(
            manager = parentFragmentManager,
            requestKey = Constants.KEY_ADD_ROLE_REQUEST_KEY
        )
    }
}