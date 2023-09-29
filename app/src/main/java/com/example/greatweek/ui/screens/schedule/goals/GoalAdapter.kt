package com.example.greatweek.ui.screens.schedule.goals

import android.content.ClipData
import android.content.ClipDescription
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.domain.model.Goal
import com.example.greatweek.ui.screens.schedule.ui.GoalItem

class GoalAdapter(
    private val goalCallback: GoalCallback,
) : ListAdapter<Goal, GoalAdapter.GoalViewHolder>(DiffCallback) {

    inner class GoalViewHolder(private val composeView: ComposeView) : RecyclerView.ViewHolder(composeView) {
        fun bind(goal: Goal) {
            composeView.setContent {
                GoalItem(
                    title = goal.title,
                    role = goal.role.toString(),
                    onLongClick = {
                        val itemId = ClipData.Item(goal.id.toString())
                        val date = goal.date.toString()
                        val dragData = ClipData(
                            date,
                            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                            itemId)
                        val dragShadowBuilder = View.DragShadowBuilder(composeView)
                        composeView.startDragAndDrop(dragData, dragShadowBuilder, composeView, 0)
                        composeView.visibility = View.INVISIBLE
                    },
                    onClick = { goalCallback.onClick(getItem(adapterPosition).id) },
                    onCheck = { goalCallback.onCompleteClick(getItem(adapterPosition).id) }
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder(ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        })
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