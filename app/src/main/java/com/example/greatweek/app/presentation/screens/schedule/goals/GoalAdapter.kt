package com.example.greatweek.app.presentation.screens.schedule.goals

import android.content.ClipData
import android.content.ClipDescription
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.databinding.GoalLayoutBinding
import com.example.greatweek.domain.model.Goal

class GoalAdapter(
    private val goalCallback: GoalCallback,
) : ListAdapter<Goal, GoalAdapter.GoalViewHolder>(DiffCallback) {

    inner class GoalViewHolder(private val binding: GoalLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding) {
                goalCheckbox.setOnClickListener {
                    goalCallback.onCompleteClick(getItem(adapterPosition).id)
                }
                root.setOnClickListener {
                    goalCallback.onClick(getItem(adapterPosition).id)
                }
            }
        }

        fun bind(goal: Goal) {
            binding.goalTextView.text = goal.title
            binding.roleTextView.text = goal.role
            binding.root.setOnLongClickListener {
                val itemId = ClipData.Item(goal.id.toString())
                val date = goal.date.toString()
                val dragData = ClipData(
                    date,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    itemId)
                val dragShadowBuilder = View.DragShadowBuilder(it)
                it.startDragAndDrop(dragData, dragShadowBuilder, it, 0)
                it.visibility = View.INVISIBLE
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder(
            GoalLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Goal>() {
            override fun areItemsTheSame(oldItem: Goal, newItem: Goal) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Goal, newItem: Goal) = oldItem == newItem
        }
    }
}