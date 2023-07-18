package com.example.greatweek.app.presentation.screens.schedule

import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.R
import com.example.greatweek.app.App
import com.example.greatweek.app.presentation.ViewModelFactory
import com.example.greatweek.app.presentation.collectFlowSafely
import com.example.greatweek.app.presentation.screens.goaldialog.GoalDialogFragment
import com.example.greatweek.app.presentation.screens.roles.RolesFragment
import com.example.greatweek.app.presentation.screens.schedule.goals.GoalCallback
import com.example.greatweek.app.presentation.screens.schedule.scrolling.OnSnapPositionChangeListener
import com.example.greatweek.app.presentation.screens.schedule.scrolling.SnapOnScrollListener
import com.example.greatweek.app.presentation.screens.schedule.scrolling.SnapOnScrollListener.Behavior
import com.example.greatweek.databinding.FragmentScheduleBinding
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.coroutines.flow.StateFlow
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class ScheduleFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ScheduleViewModel by viewModels { viewModelFactory }
    internal lateinit var binding: FragmentScheduleBinding

    private val scheduleLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    private var snapHelper = GravitySnapHelper(Gravity.CENTER)

    private val goalCallback = object : GoalCallback {
        override fun onCompleteClick(goalId: Int) = viewModel.accept(ScheduleEvent.CompleteGoal(goalId))
        override fun onClick(goalId: Int) = openEditGoalDialog(goalId)
    }

    private val scheduleCallback = object : ScheduleCallback {
        override fun onAddGoalClick(date: LocalDate) = openAddGoalDialog(date)
        override fun onGoalDrop(goalId: Int, date: LocalDate, isCommitment: Boolean) =
            viewModel.accept(ScheduleEvent.GoalDrop(goalId, date, isCommitment))
    }

    private val scheduleAdapter = ScheduleAdapter(
        goalCallback = goalCallback,
        scheduleCallback = scheduleCallback,
    )

    init {
        snapHelper.maxFlingSizeFraction = 1f
        snapHelper.scrollMsPerInch = 50f
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)

        binding.schedule.layoutManager = scheduleLayoutManager
        snapHelper.attachToRecyclerView(binding.schedule)

        binding.apply {
            schedule.addOnScrollListener(onSnapScrollListener)
            schedule.addOnScrollListener(onSnapIdleListener)
            dragZoneRight.setOnDragListener(dragListener)
            dragZoneLeft.setOnDragListener(dragListener)
        }

        childFragmentManager.beginTransaction().replace(R.id.bottom_sheet_layout, RolesFragment()).commit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.schedule.adapter = scheduleAdapter
        viewModel.scheduleState.render()
    }

    private fun StateFlow<ScheduleState>.render() = collectFlowSafely {
        collect {
            with(binding) {
                scheduleAdapter.submitList(value.schedule)
//                schedule.scrollToDate(value.currentDate)
            }
        }
    }

//    private fun RecyclerView.scrollToDate(date: LocalDate) {
//        val todayPosition = 2
//        scrollToPosition(todayPosition - 1)
//        lifecycleScope.launch(Dispatchers.Main) {
//            smoothScrollToPosition(todayPosition)
//        }
//    }

//    private suspend fun flipSchedule() {
//        snapHelper.findSnapView(scheduleLayoutManager)?.let { snapView ->
//            val snapPosition = scheduleLayoutManager.getPosition(snapView)
//            val newPosition = snapPosition + dragState.value!!
//            if (dragState.value != DRAG_STATE_IDLE) {
//                binding.schedule.smoothScrollToPosition(newPosition)
//                delay(SCROLL_DELAY_MS)
//                if (dragState.value != DRAG_STATE_IDLE)
//                    flipSchedule()
//            }
//        }
//    }

    private fun openEditGoalDialog(goalId: Int) {
        GoalDialogFragment.showForGoal(
            manager = parentFragmentManager,
            goalId = goalId,
        )
    }

    private fun openAddGoalDialog(date: LocalDate) {
        GoalDialogFragment.showForDate(
            manager = parentFragmentManager,
            date = date,
        )
    }

    private val dragListener = View.OnDragListener { view, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                if (view == binding.dragZoneRight) viewModel.accept(ScheduleEvent.DragRight)
                else viewModel.accept(ScheduleEvent.DragLeft)
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                viewModel.accept(ScheduleEvent.DragIdle)
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                viewModel.accept(ScheduleEvent.DragIdle)
                true
            }
            else -> false
        }
    }

    /**
     * Snapping
     */

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

    companion object {
        const val SCROLL_DELAY_MS = 500L
    }
}