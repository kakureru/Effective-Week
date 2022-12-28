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

class WeekAdapter(
    private val addGoal: (weekDay: Int) -> Unit,
    private val completeGoal: (goalId: Int) -> Unit,
    private val editGoal: (goalId: Int) -> Unit,
    private val dropGoal: (goalId: Int, weekDay: Int, isCommitment: Boolean) -> Unit
) : ListAdapter<WeekDay, WeekAdapter.WeekDayViewHolder>(DiffCallback) {

    inner class WeekDayViewHolder(private var binding: WeekdayCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

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
                    val weekDay = adapterPosition + 1
                    val isCommitment =
                        view == binding.commitmentsRecyclerView || view == binding.commitmentsDropTarget
                    dropGoal(goalId, weekDay, isCommitment)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.setBackgroundColor(Color.TRANSPARENT)
                    val v = event.localState as View
                    v.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }

        fun bind(weekDay: WeekDay) {
            val prioritiesList = weekDay.goals.filter { !it.commitment }
            val commitmentList = weekDay.goals.filter { it.commitment }

            val prioritiesAdapter = GoalAdapter(completeGoal, editGoal)
            val commitmentAdapter = GoalAdapter(completeGoal, editGoal)

            binding.apply {
                // View
                weekDayName.text = weekDay.name
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
                addGoalButton.setOnClickListener { addGoal(weekDay.id) }
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
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: WeekDay, newItem: WeekDay): Boolean {
                return oldItem == newItem
            }
        }
    }
}