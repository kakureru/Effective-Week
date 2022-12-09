package com.example.greatweek.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.data.repository.GoalRepositoryImpl
import com.example.greatweek.data.repository.RoleRepositoryImpl
import com.example.greatweek.databinding.FragmentScheduleBinding
import com.example.greatweek.domain.usecase.GetWeekUseCase
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.presentation.GreatWeekApplication
import com.example.greatweek.presentation.adapter.WeekAdapter
import com.example.greatweek.presentation.viewmodel.ScheduleViewModel
import com.example.greatweek.presentation.viewmodel.ScheduleViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val TAG = "ScheduleFragment"

class ScheduleFragment : Fragment() {

    // repositories
    private val goalRepository by lazy { GoalRepositoryImpl(
        (activity?.application as GreatWeekApplication).database.GoalDao()
    ) }
    private val roleRepository by lazy { RoleRepositoryImpl(
        (activity?.application as GreatWeekApplication).database.RoleDao()
    ) }

    // use cases
    private val getWeekUseCase by lazy { GetWeekUseCase(goalRepository = goalRepository) }
    private val addRoleUseCase by lazy { AddRoleUseCase(roleRepository = roleRepository) }

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private lateinit var weekRecyclerView: RecyclerView

    private val viewModel: ScheduleViewModel by activityViewModels {
        ScheduleViewModelFactory(
            getWeekUseCase,
            addRoleUseCase
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupRoleDialogFragmentListeners()
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bottomSheetLayout.scheduleFragment = this@ScheduleFragment
        }

        weekRecyclerView = binding.week
        PagerSnapHelper().attachToRecyclerView(weekRecyclerView)

        val weekAdapter = WeekAdapter()
        weekRecyclerView.adapter = weekAdapter
        GlobalScope.launch(Dispatchers.IO) {
            weekAdapter.submitList(viewModel.getWeek())
        }
    }

    private fun setupRoleDialogFragmentListeners() {
        val listener: RoleDialogListener = { requestKey, role ->
            when (requestKey) {
                KEY_ADD_ROLE_REQUEST_KEY -> addRole(role)
                KEY_EDIT_ROLE_REQUEST_KEY -> editRole()
            }
        }
        RoleDialogFragment.setupListener(parentFragmentManager, this, KEY_ADD_ROLE_REQUEST_KEY, listener)
        RoleDialogFragment.setupListener(parentFragmentManager, this, KEY_EDIT_ROLE_REQUEST_KEY, listener)
    }

    fun openAddRoleDialog(){
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
        @JvmStatic private val KEY_ADD_ROLE_REQUEST_KEY = "KEY_ADD_ROLE_REQUEST_KEY"
        @JvmStatic private val KEY_EDIT_ROLE_REQUEST_KEY = "KEY_EDIT_ROLE_REQUEST_KEY"
    }

}