package com.example.greatweek.app.presentation.adapter

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.ClipDescription
import android.content.Context
import android.graphics.Color
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.R
import com.example.greatweek.databinding.RoleCardLayoutBinding
import com.example.greatweek.domain.model.Role
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoleAdapter(
    private val renameRole: (role: Role) -> Unit,
    private val deleteRole: (role: String) -> Unit,
    private val addGoal: (role: String) -> Unit,
    private val completeGoal: (goalId: Int) -> Unit,
    private val editGoal: (goalId: Int) -> Unit,
    private val dropGoal: (goalId: Int, role: String) -> Unit,
    private val expandBottomSheet: () -> Unit
) : ListAdapter<Role, RoleAdapter.RoleViewHolder>(DiffCallback) {

    inner class RoleViewHolder(
        private val context: Context,
        private val binding: RoleCardLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val goalAdapter = GoalAdapter(completeGoal, editGoal)

        private val dragListener = View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    expandBottomSheet()
                    animateViewColor(
                        view = view,
                        colorFrom = ContextCompat.getColor(context, R.color.grey_dark),
                        colorTo = ContextCompat.getColor(context, R.color.highlight)
                    )
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    animateViewColor(
                        view = view,
                        colorFrom = ContextCompat.getColor(context, R.color.highlight),
                        colorTo = ContextCompat.getColor(context, R.color.grey_dark)
                    )
                    true
                }
                DragEvent.ACTION_DROP -> {
                    animateViewColor(
                        view = view,
                        colorFrom = ContextCompat.getColor(context, R.color.highlight),
                        colorTo = ContextCompat.getColor(context, R.color.grey_dark)
                    )
                    val item = event.clipData.getItemAt(0)
                    val goalId = item.text.toString().toInt()
                    val roleId = getItem(absoluteAdapterPosition).name
                    dropGoal(goalId, roleId)
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

        init {
            binding.apply {
                // Adapter
                goalsRecyclerView.adapter = goalAdapter
                // On drag
                goalsRecyclerView.setOnDragListener(dragListener)
                goalDropTarget.setOnDragListener(dragListener)
                // On click
                moreButton.setOnClickListener { popupMenus(it, context, getItem(absoluteAdapterPosition)) }
                addGoalButton.setOnClickListener { addGoal(getItem(absoluteAdapterPosition).name) }
            }
        }

        fun bind(role: Role) {
            val goalList = role.goals.filter { it.date == null }
            goalAdapter.submitList(goalList)

            binding.apply {
                roleTextView.text = role.name
                goalDropTarget.visibility = if (goalList.isEmpty()) View.VISIBLE else View.GONE
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
            override fun areItemsTheSame(oldItem: Role, newItem: Role): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Role, newItem: Role): Boolean {
                return oldItem.goals == newItem.goals
            }

        }
    }

    private fun popupMenus(v: View, context: Context, role: Role) {
        val popupMenus = PopupMenu(context, v)
        popupMenus.inflate(R.menu.role_menu)

        popupMenus.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    renameRole(role)
                    true
                }
                R.id.delete -> {
                    if (role.goals.isNotEmpty())
                        showRoleWarning(context)
                    else
                        deleteRole(role.name)
                    true
                }
                else -> true
            }
        }

        popupMenus.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenus)
        menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(menu, true)
    }

    private fun showRoleWarning(context: Context) {
        Toast.makeText(context, "Can't delete role with active goals", Toast.LENGTH_SHORT).show()
    }

    private fun animateViewColor(view: View, colorFrom: Int, colorTo: Int) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 250
        colorAnimation.addUpdateListener { animator ->
            view.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
    }
}