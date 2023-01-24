package com.example.greatweek.app.presentation.view

import android.content.ClipDescription
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.R
import com.example.greatweek.app.presentation.adapter.ScheduleAdapter
import com.example.greatweek.app.presentation.constants.*
import com.example.greatweek.app.presentation.utils.OnSnapPositionChangeListener
import com.example.greatweek.app.presentation.utils.SnapOnScrollListener
import com.example.greatweek.app.presentation.utils.SnapOnScrollListener.Behavior
import com.example.greatweek.app.presentation.viewmodel.ScheduleViewModel
import com.example.greatweek.databinding.FragmentScheduleBinding
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class ScheduleFragment : Fragment() {

    private val viewModel by activityViewModels<ScheduleViewModel>()

    private var _binding: FragmentScheduleBinding? = null
    val binding get() = _binding!!

    private var firstShown = true
    private var dragState = MutableLiveData(DRAG_STATE_IDLE)

    private val scheduleLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    private lateinit var scheduleAdapter: ScheduleAdapter
    private var snapHelper = GravitySnapHelper(Gravity.CENTER)

    init {
        snapHelper.maxFlingSizeFraction = 0.5f
    }

    @Suppress("RedundantNullableReturnType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)

        binding.schedule.layoutManager = scheduleLayoutManager
        snapHelper.attachToRecyclerView(binding.schedule)

        binding.apply {
            schedule.addOnScrollListener(onSnapScrollListener)
            schedule.addOnScrollListener(onSnapIdleListener)
            dragZoneRight.setOnDragListener(dragListener)
            dragZoneLeft.setOnDragListener(dragListener)
        }

        val fm: FragmentManager = childFragmentManager
        fm.beginTransaction().replace(R.id.bottom_sheet_layout, RoleTabFragment()).commit()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleAdapter = ScheduleAdapter(
            context = requireContext(),
            addGoal = { date -> openAddGoalDialog(date) },
            completeGoal = { goalId -> viewModel.completeGoal(goalId = goalId) },
            editGoal = { goalId -> openEditGoalDialog(goalId) },
            dropGoal = { goalId, date, isCommitment -> viewModel.dropGoal(goalId, date, isCommitment) }
        )
        binding.schedule.adapter = scheduleAdapter

        viewModel.schedule.observe(viewLifecycleOwner) {
            scheduleAdapter.submitList(it)

            // If its a first shown scroll to current date
            if (firstShown) {
                val todayPosition = viewModel.preloadDays.toInt()
                binding.schedule.scrollToPosition(todayPosition - 1)
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.schedule.smoothScrollToPosition(todayPosition)
                }
                firstShown = false
            }
        }

        dragState.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                scrollSchedule()
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
            requestKey = KEY_EDIT_GOAL_REQUEST_KEY
        )
    }

    private fun openAddGoalDialog(date: LocalDate) {
        GoalDialogFragment.show(
            manager = parentFragmentManager,
            argument = date,
            requestKey = KEY_ADD_GOAL_FOR_A_DAY_REQUEST_KEY
        )
    }

    /**
     * Drag and drop
     */

    // Listen when drag to the screen edges
    private val dragListener = View.OnDragListener { view, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                dragState.value = if (view == binding.dragZoneRight) DRAG_STATE_RIGHT else DRAG_STATE_LEFT
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                dragState.value = DRAG_STATE_IDLE
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                dragState.value = DRAG_STATE_IDLE
                true
            }
            else -> false
        }
    }

    // Scroll schedule on drag and drop
    private suspend fun scrollSchedule() {
        snapHelper.findSnapView(scheduleLayoutManager)?.let { snapView ->
            val snapPosition = scheduleLayoutManager.getPosition(snapView)
            val newPosition = snapPosition + dragState.value!!
            if (dragState.value != DRAG_STATE_IDLE) {
                binding.schedule.smoothScrollToPosition(newPosition)
                delay(SCROLL_DELAY_MS)
                if (dragState.value != DRAG_STATE_IDLE)
                    scrollSchedule()
            }
        }
    }

    /**
     * Snapping
     */

    // Snap position change listener

    private val onRegularSnapPositionChangeListener = object : OnSnapPositionChangeListener {
        override fun onSnapPositionChange(position: Int) {
            val day = scheduleAdapter.getItemAt(position)?.date?.dayOfWeek
            if (day != DayOfWeek.MONDAY && day != DayOfWeek.SUNDAY) {
                snapHelper.gravity = Gravity.CENTER
            }
        }
    }

    private val onEdgeSnapPositionChangeListener = object : OnSnapPositionChangeListener {
        override fun onSnapPositionChange(position: Int) {
            val day = scheduleAdapter.getItemAt(position)?.date?.dayOfWeek
            if (day == DayOfWeek.MONDAY) {
                snapHelper.gravity = Gravity.START
            } else if (day == DayOfWeek.SUNDAY) {
                snapHelper.gravity = Gravity.END
            }
        }
    }

    // Scroll listener

    private val onSnapScrollListener = SnapOnScrollListener(
        snapHelper,
        Behavior.NOTIFY_ON_SCROLL,
        onRegularSnapPositionChangeListener
    )

    private val onSnapIdleListener = SnapOnScrollListener(
        snapHelper,
        Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
        onEdgeSnapPositionChangeListener
    )
}