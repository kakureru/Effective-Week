package com.example.greatweek.app.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.greatweek.R
import com.example.greatweek.app.Constants
import com.example.greatweek.app.presentation.adapter.WeekAdapter
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModel
import com.example.greatweek.databinding.FragmentScheduleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

const val TAG = "ScheduleFragment"

class ScheduleFragment : Fragment() {

    private val viewModel by viewModel<ScheduleViewModel>()

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)

        PagerSnapHelper().attachToRecyclerView(binding.week)

        val fm: FragmentManager = childFragmentManager
        fm.beginTransaction().replace(R.id.bottom_sheet_layout, RoleTabFragment()).commit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weekAdapter = WeekAdapter(
            addGoal = { weekDay -> openAddGoalDialog(weekDay) },
            completeGoal = { goalId -> viewModel.completeGoal(goalId = goalId) },
            editGoal = { goalId -> openEditGoalDialog(goalId) },
            dropGoal = { goalId, weekDay, isCommitment -> viewModel.dropGoal(goalId, weekDay, isCommitment) }
        )
        binding.week.adapter = weekAdapter
        viewModel.week.observe(viewLifecycleOwner) {
            weekAdapter.submitList(it)
        }
    }

    private fun openEditGoalDialog(goalId: Int) {
        GoalDialogFragment.show(
            manager = parentFragmentManager,
            argument = goalId,
            requestKey = Constants.KEY_EDIT_GOAL_REQUEST_KEY
        )
    }

    private fun openAddGoalDialog(weekDay: Int) {
        GoalDialogFragment.show(
            manager = parentFragmentManager,
            argument = weekDay,
            requestKey = Constants.KEY_ADD_GOAL_FOR_A_DAY_REQUEST_KEY
        )
    }

}