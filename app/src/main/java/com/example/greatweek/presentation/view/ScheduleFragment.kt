package com.example.greatweek.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.data.repository.GoalRepositoryImpl
import com.example.greatweek.databinding.FragmentScheduleBinding
import com.example.greatweek.domain.usecase.GetWeekUseCase
import com.example.greatweek.presentation.GreatWeekApplication
import com.example.greatweek.presentation.adapter.WeekAdapter
import com.example.greatweek.presentation.viewmodel.ScheduleViewModel
import com.example.greatweek.presentation.viewmodel.ScheduleViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ScheduleFragment : Fragment() {

    private val goalRepository by lazy { GoalRepositoryImpl(
        (activity?.application as GreatWeekApplication).database.GoalDao()
    ) }
    private val getWeekUseCase by lazy { GetWeekUseCase(goalRepository = goalRepository) }

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private lateinit var weekRecyclerView: RecyclerView

    private val viewModel: ScheduleViewModel by activityViewModels {
        ScheduleViewModelFactory(getWeekUseCase)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weekRecyclerView = binding.week
        PagerSnapHelper().attachToRecyclerView(weekRecyclerView)

        val weekAdapter = WeekAdapter()
        weekRecyclerView.adapter = weekAdapter
        GlobalScope.launch(Dispatchers.IO) {
            weekAdapter.submitList(viewModel.week)
        }

    }

}