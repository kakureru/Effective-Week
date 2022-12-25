package com.example.greatweek.app.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.greatweek.databinding.FragmentRoleTabBinding
import com.example.greatweek.domain.model.Role
import com.example.greatweek.app.Constants
import com.example.greatweek.app.presentation.adapter.RoleAdapter
import com.example.greatweek.app.presentation.viewmodel.RoleTabViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RoleTabFragment : Fragment() {

    private val viewModel by viewModel<RoleTabViewModel>()

    private var _binding: FragmentRoleTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoleTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            roleTabFragment = this@RoleTabFragment
        }
        val roleAdapter = RoleAdapter(
            renameRole = { role -> openRenameRoleDialog(role) },
            deleteRole = { roleId -> viewModel.deleteRole(roleId = roleId) },
            addGoal = { roleId -> openAddGoalDialog(roleId) },
            completeGoal = { goalId -> viewModel.completeGoal(goalId = goalId) },
            editGoal = { goalId -> openEditGoalDialog(goalId) }
        )
        binding.rolesRecyclerView.adapter = roleAdapter
        viewModel.allRoles.observe(viewLifecycleOwner) { roles ->
            roles?.let { roleAdapter.submitList(roles) }
        }
        registerForContextMenu(binding.rolesRecyclerView)
    }

    private fun openEditGoalDialog(goalId: Int) {
        GoalDialogFragment.show(
            manager = parentFragmentManager,
            argument = goalId,
            requestKey = Constants.KEY_EDIT_GOAL_REQUEST_KEY
        )
    }

    private fun openAddGoalDialog(roleId: Int) {
        GoalDialogFragment.show(
            manager = parentFragmentManager,
            argument = roleId,
            requestKey = Constants.KEY_ADD_GOAL_FOR_A_ROLE_REQUEST_KEY
        )
    }

    private fun openRenameRoleDialog(role: Role) {
        RoleDialogFragment.show(
            manager = parentFragmentManager,
            roleId = role.id,
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