package com.example.greatweek.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.databinding.WeekdayCardLayoutBinding
import com.example.greatweek.domain.model.WeekDay

class WeekAdapter(
    private val addGoal: (weekDay: Int) -> Unit,
    private val completeGoal: (goalId: Int) -> Unit
) : ListAdapter<WeekDay, WeekAdapter.WeekDayViewHolder>(DiffCallback) {

    inner class WeekDayViewHolder(private var binding: WeekdayCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weekDay: WeekDay) {
            binding.weekDayName.text = weekDay.name

            // goal adapter
            val goalAdapter = GoalAdapter(completeGoal = completeGoal)
            binding.prioritiesRecyclerView.adapter = goalAdapter
            goalAdapter.submitList(weekDay.goals.filter { goal ->
                !goal.commitment
            })

            // commitment adapter
            val commitmentAdapter = GoalAdapter(completeGoal = completeGoal)
            binding.commitmentsRecyclerView.adapter = commitmentAdapter
            commitmentAdapter.submitList(weekDay.goals.filter { goal ->
                goal.commitment
            })

            binding.addGoalButton.setOnClickListener {
                addGoal(weekDay.id)
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
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: WeekDay, newItem: WeekDay): Boolean {
                return oldItem == newItem
            }
        }
    }
}