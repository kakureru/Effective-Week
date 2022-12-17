package com.example.greatweek.app.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.example.greatweek.databinding.FragmentRoleTabBinding
import com.example.greatweek.domain.model.Role
import com.example.greatweek.app.Constants
import com.example.greatweek.app.presentation.adapter.RoleAdapter
import com.example.greatweek.app.presentation.viewmodel.RoleTabViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
            deleteRole = { roleId -> deleteRole(roleId) },
            addGoal = { roleId -> openAddGoalDialog(roleId) },
            completeGoal = { goalId -> completeGoal(goalId) }
        )
        binding.rolesRecyclerView.adapter = roleAdapter
        lifecycle.coroutineScope.launch {
            viewModel.getRoles().collect {
                roleAdapter.submitList(it)
                roleAdapter.notifyDataSetChanged()
            }
        }
        registerForContextMenu(binding.rolesRecyclerView)
    }

    private fun completeGoal(goalId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.completeGoal(goalId = goalId)
        }
    }

    private fun deleteRole(roleId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.deleteRole(roleId = roleId)
        }
    }

    private fun openAddGoalDialog(roleId: Int) {
        GoalDialogFragment.show(
            manager = parentFragmentManager,
            roleId = roleId,
            requestKey = Constants.KEY_ADD_GOAL_REQUEST_KEY
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