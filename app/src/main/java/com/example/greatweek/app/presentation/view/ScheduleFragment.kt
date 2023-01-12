package com.example.greatweek.app.presentation.view

import android.content.ClipDescription
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.R
import com.example.greatweek.app.Constants
import com.example.greatweek.app.presentation.adapter.WeekAdapter
import com.example.greatweek.app.presentation.view.utils.OnSnapPositionChangeListener
import com.example.greatweek.app.presentation.view.utils.SnapOnScrollListener
import com.example.greatweek.app.presentation.view.utils.SnapOnScrollListener.Behavior
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModel
import com.example.greatweek.databinding.FragmentScheduleBinding
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.DayOfWeek
import java.time.LocalDate

class ScheduleFragment : Fragment() {

    private val viewModel by activityViewModel<ScheduleViewModel>()

    private var _binding: FragmentScheduleBinding? = null
    val binding get() = _binding!!

    private var firstShown = true

    private lateinit var weekAdapter: WeekAdapter

    private val weekLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    private var snapHelper = GravitySnapHelper(Gravity.CENTER)
    private var scrollDirection = MutableLiveData(0)

    private val dragListener = View.OnDragListener { view, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                scrollDirection.value = if (view == binding.dragZoneRight) 1 else -1
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                scrollDirection.value = 0
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                scrollDirection.value = 0
                true
            }
            else -> false
        }
    }

    private val onSnapPositionChangeListener: OnSnapPositionChangeListener =
        object : OnSnapPositionChangeListener {
            override fun onSnapPositionChange(position: Int) {
                val day = weekAdapter.getItemAt(position)?.date?.dayOfWeek
                if (day != DayOfWeek.MONDAY && day != DayOfWeek.SUNDAY) {
                    snapHelper.gravity = Gravity.CENTER
                }
            }
        }

    private val onIdleSnapPositionChangeListener: OnSnapPositionChangeListener =
        object : OnSnapPositionChangeListener {
            override fun onSnapPositionChange(position: Int) {
                val day = weekAdapter.getItemAt(position)?.date?.dayOfWeek
                if (day == DayOfWeek.MONDAY) {
                    snapHelper.gravity = Gravity.START
                } else if (day == DayOfWeek.SUNDAY) {
                    snapHelper.gravity = Gravity.END
                }
            }
        }

    private val onSnapScrollListener = SnapOnScrollListener(
        snapHelper,
        Behavior.NOTIFY_ON_SCROLL,
        onSnapPositionChangeListener
    )

    private val onIdleSnapScrollListener = SnapOnScrollListener(
        snapHelper,
        Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
        onIdleSnapPositionChangeListener
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        binding.week.layoutManager = weekLayoutManager
        // Configure snap helper
        snapHelper.attachToRecyclerView(binding.week)
        snapHelper.maxFlingSizeFraction = 0.5f
        binding.week.apply {
            addOnScrollListener(onSnapScrollListener)
            addOnScrollListener(onIdleSnapScrollListener)
        }
        // Attach role tab fragment
        val fm: FragmentManager = childFragmentManager
        fm.beginTransaction().replace(R.id.bottom_sheet_layout, RoleTabFragment()).commit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Week adapter
        weekAdapter = WeekAdapter(
            context = requireContext(),
            addGoal = { date -> openAddGoalDialog(date) },
            completeGoal = { goalId -> viewModel.completeGoal(goalId = goalId) },
            editGoal = { goalId -> openEditGoalDialog(goalId) },
            dropGoal = { goalId, date, isCommitment -> viewModel.dropGoal(goalId, date, isCommitment) }
        )
        binding.week.adapter = weekAdapter

        binding.dragZoneRight.setOnDragListener(dragListener)
        binding.dragZoneLeft.setOnDragListener(dragListener)

        viewModel.schedule.observe(viewLifecycleOwner) {
            weekAdapter.submitList(it)
            weekAdapter.notifyDataSetChanged()
            if (firstShown) {
                val todayPosition = viewModel.preloadDays.toInt()
                binding.week.scrollToPosition(todayPosition - 1)
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.week.smoothScrollToPosition(todayPosition)
                }
                firstShown = false
            }
        }

        scrollDirection.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                scrollWeek()
            }
        }
    }

    private suspend fun scrollWeek() {
        snapHelper.findSnapView(weekLayoutManager)?.let { snapView ->
            val snapPosition = weekLayoutManager.getPosition(snapView)
            val newPosition = snapPosition + scrollDirection.value!!
            if (scrollDirection.value != 0) {
                binding.week.smoothScrollToPosition(newPosition)
                delay(Constants.SCROLL_DELAY_MS)
                if (scrollDirection.value != 0)
                    scrollWeek()
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