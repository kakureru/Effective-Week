package com.example.greatweek.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.greatweek.R
import com.example.greatweek.data.repository.GoalRepositoryImpl
import com.example.greatweek.databinding.FragmentScheduleBinding
import com.example.greatweek.domain.usecase.goal.GetWeekUseCase
import com.example.greatweek.presentation.GreatWeekApplication
import com.example.greatweek.presentation.adapter.WeekAdapter
import com.example.greatweek.presentation.viewmodel.ScheduleViewModel
import com.example.greatweek.presentation.viewmodel.ScheduleViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val TAG = "ScheduleFragment"

class ScheduleFragment : Fragment() {

    // repository
    private val goalRepository by lazy {
        GoalRepositoryImpl(
            (activity?.application as GreatWeekApplication).database.GoalDao()
        )
    }

    // use cases
    private val getWeekUseCase by lazy { GetWeekUseCase(goalRepository = goalRepository) }

    // binding
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    // viewModel
    private val viewModel: ScheduleViewModel by activityViewModels {
        ScheduleViewModelFactory(
            getWeekUseCase
        )
    }

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

        val weekAdapter = WeekAdapter()
        binding.week.adapter = weekAdapter
        GlobalScope.launch(Dispatchers.IO) {
            weekAdapter.submitList(viewModel.getWeek())
        }
    }

}