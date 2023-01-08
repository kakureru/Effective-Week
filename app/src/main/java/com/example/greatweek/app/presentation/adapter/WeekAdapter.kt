package com.example.greatweek.app.presentation.adapter

import android.content.ClipDescription
import android.graphics.Color
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.databinding.WeekdayCardLayoutBinding
import com.example.greatweek.domain.model.WeekDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeekAdapter(
    private val addGoal: (date: LocalDate) -> Unit,
    private val completeGoal: (goalId: Int) -> Unit,
    private val editGoal: (goalId: Int) -> Unit,
    private val dropGoal: (goalId: Int, date: LocalDate, isCommitment: Boolean) -> Unit
) : ListAdapter<WeekDay, WeekAdapter.WeekDayViewHolder>(DiffCallback) {

    inner class WeekDayViewHolder(private var binding: WeekdayCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weekDay: WeekDay) {
            val dragListener = View.OnDragListener { view, event ->
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
                        dropGoal(goalId, weekDay.date, isCommitment)
                        true
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        view.setBackgroundColor(Color.TRANSPARENT)
                        if (!event.result)
                            (event.localState as View).visibility = View.VISIBLE
                        true
                    }
                    else -> false
                }
            }

            val prioritiesList = weekDay.goals.filter { !it.commitment }
            val commitmentList = weekDay.goals.filter { it.commitment }

            val prioritiesAdapter = GoalAdapter(completeGoal, editGoal)
            val commitmentAdapter = GoalAdapter(completeGoal, editGoal)

            binding.apply {
                // View
                weekDayName.text = DateTimeFormatter.ofPattern("EEEE").format(weekDay.date)
                    .replaceFirstChar { it.uppercase() }
                dateTextView.text = DateTimeFormatter.ofPattern("MMM d").format(weekDay.date)
                    .replaceFirstChar { it.uppercase() }
                prioritiesDropTarget.visibility =
                    if (prioritiesList.isEmpty()) View.VISIBLE else View.GONE
                commitmentsDropTarget.visibility =
                    if (commitmentList.isEmpty()) View.VISIBLE else View.GONE
                // Adapter
                prioritiesRecyclerView.adapter = prioritiesAdapter
                commitmentsRecyclerView.adapter = commitmentAdapter
                // On drag listener
                prioritiesRecyclerView.setOnDragListener(dragListener)
                commitmentsRecyclerView.setOnDragListener(dragListener)
                prioritiesDropTarget.setOnDragListener(dragListener)
                commitmentsDropTarget.setOnDragListener(dragListener)
                // On click listener
                addGoalButton.setOnClickListener { addGoal(weekDay.date) }
            }

            prioritiesAdapter.submitList(prioritiesList)
            commitmentAdapter.submitList(commitmentList)
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