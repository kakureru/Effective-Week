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
import com.example.greatweek.domain.model.Goal
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
                DragEvent.ACTION_DRAG_LOCATION -> {
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    view.setBackgroundColor(Color.TRANSPARENT)
                    true
                }
                DragEvent.ACTION_DROP -> {
//                    val v = event.localState as View
//                    val owner = v.parent as ViewGroup
//                    owner.removeView(v)
//                    val destination = view as RecyclerView
//                    destination.addView(v)
                    val item = event.clipData.getItemAt(0)
                    val goalId = item.text.toString().toInt()
                    val weekDay = adapterPosition + 1
                    val isCommitment = view != binding.prioritiesRecyclerView
                    dropGoal(goalId, weekDay, isCommitment)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.setBackgroundColor(Color.TRANSPARENT)
                    val v = event.localState as View
                    v.visibility = View.VISIBLE
                    //view.invalidate()
                    true
                }
                else -> false
            }
        }

        fun bind(weekDay: WeekDay) {
            binding.weekDayName.text = weekDay.name

            // goal adapter
            val goalAdapter = GoalAdapter(
                completeGoal = completeGoal,
                editGoal = editGoal
            )
            binding.prioritiesRecyclerView.adapter = goalAdapter
            goalAdapter.submitList(weekDay.goals.filter { goal ->
                !goal.commitment
            })
            binding.prioritiesRecyclerView.setOnDragListener(dragListener)

            // commitment adapter
            val commitmentAdapter = GoalAdapter(
                completeGoal = completeGoal,
                editGoal = editGoal
            )
            binding.commitmentsRecyclerView.adapter = commitmentAdapter
            commitmentAdapter.submitList(weekDay.goals.filter { goal ->
                goal.commitment
            })
            binding.commitmentsRecyclerView.setOnDragListener(dragListener)

            binding.addGoalButton.setOnClickListener { addGoal(weekDay.id) }
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