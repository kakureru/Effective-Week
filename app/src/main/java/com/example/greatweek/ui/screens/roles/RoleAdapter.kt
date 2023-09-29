package com.example.greatweek.ui.screens.roles

import android.content.ClipDescription
import android.content.Context
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.ui.screens.schedule.goals.GoalAdapter
import com.example.greatweek.ui.screens.schedule.goals.GoalCallback
import com.example.greatweek.databinding.RoleCardLayoutBinding
import com.example.greatweek.domain.model.Role

class RoleAdapter(
    private val goalCallback: GoalCallback,
    private val roleCallback: RoleCallback,
    private val roleDragListenerCallback: RoleDragListenerCallback,
) : ListAdapter<Role, RoleAdapter.RoleViewHolder>(DiffCallback) {

    inner class RoleViewHolder(
        private val context: Context,
        private val binding: RoleCardLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val goalAdapter = GoalAdapter(goalCallback)

        private val dragListener = View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                DragEvent.ACTION_DRAG_ENTERED -> roleDragListenerCallback.onDragEntered(view)
                DragEvent.ACTION_DRAG_EXITED -> roleDragListenerCallback.onDragExited(view)
                DragEvent.ACTION_DROP -> roleDragListenerCallback.onDrop(view, event, role = getItem(adapterPosition).name)
                DragEvent.ACTION_DRAG_ENDED -> roleDragListenerCallback.onDragEnded(view, event)
                else -> false
            }
        }

        init {
            binding.apply {
                goalsRecyclerView.adapter = goalAdapter
                goalsRecyclerView.setOnDragListener(dragListener)
                goalDropTarget.setOnDragListener(dragListener)
                moreButton.setOnClickListener { roleCallback.onMoreClick(it, context, getItem(adapterPosition)) }
                addGoalButton.setOnClickListener { roleCallback.onAddGoalClick(getItem(adapterPosition).name) }
            }
        }

        fun bind(role: Role) {
            goalAdapter.submitList(role.goals)
            binding.apply {
                roleTextView.text = role.name
                goalDropTarget.isVisible = role.goals.isEmpty()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        return RoleViewHolder(
            parent.context,
            RoleCardLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Role>() {
            override fun areItemsTheSame(oldItem: Role, newItem: Role) = oldItem.name == newItem.name
            override fun areContentsTheSame(oldItem: Role, newItem: Role) = oldItem.goals == newItem.goals
        }
    }
}