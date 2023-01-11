package com.example.greatweek.app.presentation.adapter

import android.content.ClipDescription
import android.content.Context
import android.graphics.Color
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.R
import com.example.greatweek.databinding.WeekdayCardLayoutBinding
import com.example.greatweek.domain.model.WeekDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeekAdapter(
    private val context: Context,
    private val addGoal: (date: LocalDate) -> Unit,
    private val completeGoal: (goalId: Int) -> Unit,
    private val editGoal: (goalId: Int) -> Unit,
    private val dropGoal: (goalId: Int, date: LocalDate, isCommitment: Boolean) -> Unit
) : ListAdapter<WeekDay, WeekAdapter.WeekDayViewHolder>(DiffCallback) {

    private val today = LocalDate.now()

    inner class WeekDayViewHolder(private var binding: WeekdayCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val prioritiesAdapter = GoalAdapter(completeGoal, editGoal)
        private val commitmentAdapter = GoalAdapter(completeGoal, editGoal)

        private val dragListener = View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.setBackgroundColor(Color.GRAY)
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    view.setBackgroundColor(Color.TRANSPARENT)
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val item = event.clipData.getItemAt(0)
                    val goalId = item.text.toString().toInt()
                    val isCommitment =
                        view == binding.commitmentsRecyclerView || view == binding.commitmentsDropTarget
                    dropGoal(goalId, getItem(adapterPosition).date, isCommitment)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.setBackgroundColor(Color.TRANSPARENT)
                    (event.localState as View).visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }

        init {
            binding.apply {
                // On drag
                prioritiesRecyclerView.setOnDragListener(dragListener)
                commitmentsRecyclerView.setOnDragListener(dragListener)
                prioritiesDropTarget.setOnDragListener(dragListener)
                commitmentsDropTarget.setOnDragListener(dragListener)
                // On click
                addGoalButton.setOnClickListener { addGoal(getItem(adapterPosition).date) }
                // Adapter
                prioritiesRecyclerView.adapter = prioritiesAdapter
                commitmentsRecyclerView.adapter = commitmentAdapter
            }
        }

        fun bind(day: WeekDay) {
            val priorities = day.goals.filter { !it.commitment }
            val commitments = day.goals.filter { it.commitment }

            prioritiesAdapter.submitList(priorities)
            commitmentAdapter.submitList(commitments)

            val weekDay = DateTimeFormatter.ofPattern("EEEE").format(day.date)
                .replaceFirstChar { it.uppercase() }
            val weekDayColor =
                if (day.date == today) ContextCompat.getColor(context, R.color.highlight)
                else Color.WHITE
            val date = DateTimeFormatter.ofPattern("MMM d").format(day.date)
                .replaceFirstChar { it.uppercase() }

            binding.apply {
                weekDayTextView.text = weekDay
                weekDayTextView.setTextColor(weekDayColor)
                dateTextView.text = date
                prioritiesDropTarget.visibility =
                    if (priorities.isEmpty()) View.VISIBLE else View.GONE
                commitmentsDropTarget.visibility =
                    if (commitments.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekDayViewHolder {
        return WeekDayViewHolder(
            WeekdayCardLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeekDayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<WeekDay>() {
            override fun areItemsTheSame(oldItem: WeekDay, newItem: WeekDay): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: WeekDay, newItem: WeekDay): Boolean {
                return oldItem == newItem
            }
        }
    }
}