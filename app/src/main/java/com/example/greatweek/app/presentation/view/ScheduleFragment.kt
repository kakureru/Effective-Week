package com.example.greatweek.app.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.greatweek.R
import com.example.greatweek.app.Constants
import com.example.greatweek.app.presentation.adapter.WeekAdapter
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModel
import com.example.greatweek.databinding.FragmentScheduleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.LocalDate

class ScheduleFragment : Fragment() {

    private val viewModel by activityViewModel<ScheduleViewModel>()

    private var _binding: FragmentScheduleBinding? = null
    val binding get() = _binding!!

    private var snapHelper = PagerSnapHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        snapHelper.attachToRecyclerView(binding.week)
        val fm: FragmentManager = childFragmentManager
        fm.beginTransaction().replace(R.id.bottom_sheet_layout, RoleTabFragment()).commit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weekAdapter = WeekAdapter(
            context = requireContext(),
            addGoal = { date -> openAddGoalDialog(date) },
            completeGoal = { goalId -> viewModel.completeGoal(goalId = goalId) },
            editGoal = { goalId -> openEditGoalDialog(goalId) },
            dropGoal = { goalId, date, isCommitment -> viewModel.dropGoal(goalId, date, isCommitment) }
        )
        binding.week.adapter = weekAdapter
        var firstShown = true
        viewModel.week.observe(viewLifecycleOwner) {
            weekAdapter.submitList(it)
            weekAdapter.notifyDataSetChanged()
            if (firstShown) {
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.week.smoothScrollToPosition(viewModel.today.dayOfWeek.value - 1)
                    firstShown = false
                }
            }
        }
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

    private fun openAddGoalDialog(date: LocalDate) {
        GoalDialogFragment.show(
            manager = parentFragmentManager,
            argument = date,
            requestKey = Constants.KEY_ADD_GOAL_FOR_A_DAY_REQUEST_KEY
        )
    }
}