package com.example.greatweek.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.greatweek.data.repository.RoleRepositoryImpl
import com.example.greatweek.databinding.FragmentRoleTabBinding
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.domain.usecase.role.GetRolesUseCase
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
    private val addRoleUseCase by lazy { AddRoleUseCase(roleRepository) }

    // binding
    private var _binding: FragmentRoleTabBinding? = null
    private val binding get() = _binding!!

    // viewModel
    private val viewModel: RoleTabViewModel by activityViewModels {
        RoleTabViewModelFactory(
            getRolesUseCase = getRolesUseCase,
            addRoleUseCase = addRoleUseCase
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupRoleDialogFragmentListeners()
        _binding = FragmentRoleTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            roleTabFragment = this@RoleTabFragment
        }

        val roleAdapter = RoleAdapter()
        binding.rolesRecyclerView.adapter = roleAdapter
        GlobalScope.launch(Dispatchers.IO) {
            roleAdapter.submitList(viewModel.getRoles())
        }
    }

    private fun setupRoleDialogFragmentListeners() {
        val listener: RoleDialogListener = { requestKey, role ->
            when (requestKey) {
                KEY_ADD_ROLE_REQUEST_KEY -> addRole(role)
                KEY_EDIT_ROLE_REQUEST_KEY -> editRole()
            }
        }
        RoleDialogFragment.setupListener(
            parentFragmentManager,
            this,
            KEY_ADD_ROLE_REQUEST_KEY,
            listener
        )
        RoleDialogFragment.setupListener(
            parentFragmentManager,
            this,
            KEY_EDIT_ROLE_REQUEST_KEY,
            listener
        )
    }

    fun openAddRoleDialog() {
        RoleDialogFragment.show(parentFragmentManager, "", KEY_ADD_ROLE_REQUEST_KEY)
    }

    private fun addRole(name: String) {
        Toast.makeText(context, "Role Added", Toast.LENGTH_SHORT).show()
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.addRole(name = name)
        }
    }

    private fun editRole() {
        // TODO() запустить редактирование роли
    }

    companion object {
        @JvmStatic
        private val KEY_ADD_ROLE_REQUEST_KEY = "KEY_ADD_ROLE_REQUEST_KEY"

        @JvmStatic
        private val KEY_EDIT_ROLE_REQUEST_KEY = "KEY_EDIT_ROLE_REQUEST_KEY"
    }
}