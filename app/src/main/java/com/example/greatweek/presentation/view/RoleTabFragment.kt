package com.example.greatweek.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import com.example.greatweek.data.repository.GoalRepositoryImpl
import com.example.greatweek.data.repository.RoleRepositoryImpl
import com.example.greatweek.databinding.FragmentRoleTabBinding
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.domain.model.Role
import com.example.greatweek.domain.usecase.goal.AddGoalForTheRoleUseCase
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.domain.usecase.role.DeleteRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesUseCase
import com.example.greatweek.domain.usecase.role.RenameRoleUseCase
import com.example.greatweek.presentation.Constants
import com.example.greatweek.presentation.GreatWeekApplication
import com.example.greatweek.presentation.adapter.RoleAdapter
import com.example.greatweek.presentation.viewmodel.RoleTabViewModel
import com.example.greatweek.presentation.viewmodel.RoleTabViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RoleTabFragment : Fragment() {

    // repository
    private val roleRepository by lazy {
        RoleRepositoryImpl(
            (activity?.application as GreatWeekApplication).database.RoleDao()
        )
    }

    // use cases
    private val getRolesUseCase by lazy { GetRolesUseCase(roleRepository) }
    private val deleteRoleUseCase by lazy { DeleteRoleUseCase(roleRepository) }

    // viewModel
    private val viewModel: RoleTabViewModel by activityViewModels {
        RoleTabViewModelFactory(
            getRolesUseCase = getRolesUseCase,
            deleteRoleUseCase = deleteRoleUseCase)
    }

    // binding
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
            { role -> openRenameRoleDialog(role) },
            { roleId -> deleteRole(roleId) }
        )
        binding.rolesRecyclerView.adapter = roleAdapter
        lifecycle.coroutineScope.launch {
            viewModel.getRoles().collect() {
                roleAdapter.submitList(it)
            }
        }
        registerForContextMenu(binding.rolesRecyclerView)
    }

    private fun deleteRole(roleId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.deleteRole(roleId = roleId)
        }
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