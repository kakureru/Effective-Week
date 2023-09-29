package com.example.greatweek.ui.screens.schedule

import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.greatweek.App
import com.example.greatweek.R
import com.example.greatweek.databinding.FragmentScheduleBinding
import com.example.greatweek.ui.ViewModelFactory
import com.example.greatweek.ui.screens.goaldialog.GoalDialogFragment
import com.example.greatweek.ui.screens.roles.RolesFragment
import com.example.greatweek.ui.screens.schedule.goals.GoalCallback
import com.example.greatweek.ui.screens.schedule.ui.ScheduleDay
import com.example.greatweek.ui.theme.DarkTheme
import java.time.LocalDate
import javax.inject.Inject

class ScheduleFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ScheduleViewModel by viewModels { viewModelFactory }
    internal lateinit var binding: FragmentScheduleBinding

    private val goalCallback = object : GoalCallback {
        override fun onCompleteClick(goalId: Int) = viewModel.accept(ScheduleEvent.CompleteGoal(goalId))
        override fun onClick(goalId: Int) = openEditGoalDialog(goalId)
    }

    private val scheduleCallback = object : ScheduleCallback {
        override fun onAddGoalClick(date: LocalDate) = openAddGoalDialog(date)
        override fun onGoalDrop(goalId: Int, date: LocalDate, isCommitment: Boolean) =
            viewModel.accept(ScheduleEvent.GoalDrop(goalId, date, isCommitment))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as App).appComponent.inject(this)
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentScheduleBinding.inflate(inflater, container, false).apply {
            dragZoneRight.setOnDragListener(dragListener)
            dragZoneLeft.setOnDragListener(dragListener)

            schedule.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            }.setContent {
                val state by viewModel.scheduleState.collectAsState()
                DarkTheme {
                    val rowState = rememberLazyListState()
                    val snapBehavior = rememberSnapFlingBehavior(lazyListState = rowState)
                    LazyRow(
                        state = rowState,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        flingBehavior = snapBehavior,
                    ) {
                        items(items = state.schedule, key = { item -> item.dateText }) {
                            ScheduleDay(
                                weekday = it.weekday,
                                date = it.dateText,
                                isToday = it.isToday,
                                priorities = it.priorities,
                                appointments = it.appointments,
                                onAddGoalClick = { scheduleCallback.onAddGoalClick(it.date) },
                                goalCallback = goalCallback
                            )
                        }
                    }
                }
            }
        }
        childFragmentManager.beginTransaction().replace(R.id.bottom_sheet_layout, RolesFragment()).commit()
        return binding.root
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

    companion object {
        const val SCROLL_DELAY_MS = 500L
    }
}